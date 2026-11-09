package com.macrivo.app.presentation.foodbrowse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macrivo.app.core.utils.Resource
import com.macrivo.app.domain.calculator.ToastState
import com.macrivo.app.domain.model.CustomToastProperty
import com.macrivo.app.domain.model.FoodItem
import com.macrivo.app.domain.model.Serving
import com.macrivo.app.domain.model.foodlog.LogFoodRequest
import com.macrivo.app.domain.model.scale
import com.macrivo.app.domain.use_case.food.GetFoodByIdUseCase
import com.macrivo.app.domain.use_case.food.ObserveIsFoodSavedUseCase
import com.macrivo.app.domain.use_case.food.RemoveSavedFoodUseCase
import com.macrivo.app.domain.use_case.food.SaveFoodUseCase
import com.macrivo.app.domain.use_case.food.SearchFoodsUseCase
import com.macrivo.app.domain.use_case.foodlog.LogFoodUseCase
import com.macrivo.app.presentation.commoncomponent.ErrorToast
import com.macrivo.app.presentation.commoncomponent.SuccessToast
import com.macrivo.app.presentation.foodbrowse.component.FoodBrowseEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

private const val TAG = "FoodBrowseViewModel"
private const val SEARCH_LIMIT = 38
private const val SEARCH_DEBOUNCE_MS = 300L

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class FoodBrowseViewModel @Inject constructor(
    private val searchFoods: SearchFoodsUseCase,
    private val getFoodById: GetFoodByIdUseCase,
    private val foodLog: LogFoodUseCase,
    private val observeIsFoodSaved: ObserveIsFoodSavedUseCase,
    private val saveFood: SaveFoodUseCase,
    private val removeSavedFood: RemoveSavedFoodUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(FoodBrowseState(isLoading = true))
    val state: StateFlow<FoodBrowseState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<FoodBrowseEvent>()
    val events = _events.asSharedFlow()

    val foodDetailUiState: StateFlow<FoodDetailUiState> = _state
        .flatMapLatest { s ->
            when {
                s.isDetailLoading -> flowOf(FoodDetailUiState.Loading)

                s.detailError != null -> flowOf(FoodDetailUiState.Error(s.detailError))

                s.selectedFood != null && s.selectedServing != null -> {
                    val food = s.selectedFood
                    val serving = s.selectedServing
                    observeIsFoodSaved(food.id).map { isSaved ->
                        FoodDetailUiState.Success(
                            foodName = food.name,
                            foodBrand = food.brand,
                            servings = food.servings,
                            selectedServing = serving,
                            quantity = s.quantity,
                            scaledNutrition = serving.nutrition.scale(s.quantity),
                            isSaved = isSaved,
                        )
                    }
                }

                else -> flowOf(FoodDetailUiState.Loading)
            }
        }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = FoodDetailUiState.Loading,
        )

    init {
        Log.d(TAG, "init")
        observeSearchQuery()
    }

    private fun observeSearchQuery() {
        state
            .map { it.query.trim() }
            .distinctUntilChanged()
            .flatMapLatest { query ->
                flow {
                    if (query.isNotBlank()) {
                        delay(SEARCH_DEBOUNCE_MS)
                    }
                    emitAll(searchFoods(query, SEARCH_LIMIT))
                }
            }
            .onEach(::handleSearchResult)
            .catch { exception ->
                if (exception is CancellationException) throw exception
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = searchErrorMessage(
                            responseMessage = null,
                            hasVisibleResults = it.foods.isNotEmpty(),
                        ),
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
    }

    private var detailJob: Job? = null

    fun onQueryChange(query: String) {
        _state.update {
            val queryChanged = it.query.trim().lowercase(Locale.ROOT) !=
                    query.trim().lowercase(Locale.ROOT)
            it.copy(
                query = query,
                foods = if (queryChanged) emptyList() else it.foods,
                isLoading = queryChanged,
                error = null,
            )
        }
    }

    private fun handleSearchResult(result: Resource<List<FoodItem>>) {
        when (result) {
            is Resource.Loading -> {
                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null,
                    )
                }
            }

            is Resource.Success -> {
                _state.update {
                    it.copy(
                        foods = result.data ?: emptyList(),
                        isLoading = false,
                        error = null,
                    )
                }
            }

            is Resource.Error -> {
                Log.w(TAG, result.message ?: "Food search failed")
                _state.update {
                    it.copy(
                        error = searchErrorMessage(
                            responseMessage = result.message,
                            hasVisibleResults = it.foods.isNotEmpty(),
                        ),
                        isLoading = false,
                    )
                }
            }
        }
    }

    private fun searchErrorMessage(
        responseMessage: String?,
        hasVisibleResults: Boolean,
    ): String = when {
        hasVisibleResults -> "Couldn't refresh foods. Showing the saved results."
        responseMessage?.contains("unexpected response", ignoreCase = true) == true ->
            "The food service returned an unexpected response. Please try again."
        else -> "Couldn't load foods. Check your connection and try again."
    }

    fun openFoodDetail(foodId: String) {
        _state.update { it.copy(destination = FoodBrowseDestination.Detail(foodId), isManuallySelected = true) }
        loadDetail(foodId, manuallySelected = true)
    }

    fun onFirstVisibleFoodChanged(foodId: String) {
        val current = _state.value
        if (current.isManuallySelected) return
        if (current.selectedFood?.id == foodId) return
        loadDetail(foodId, manuallySelected = false)
    }

    fun onServingSelected(serving: Serving) {
        _state.update { it.copy(selectedServing = serving, quantity = 1f) }
    }

    fun onQuantityChanged(quantity: Float) {
        _state.update { it.copy(quantity = quantity) }
    }

    fun onBackFromDetail() {
        _state.update {
            it.copy(
                destination = FoodBrowseDestination.List,
                selectedFood = null,
                selectedServing = null,
                quantity = 1f,
                isDetailLoading = false,
                detailError = null,
                isManuallySelected = false,
            )
        }
    }

    private fun loadDetail(foodId: String, manuallySelected: Boolean) {
        detailJob?.cancel()

        detailJob = viewModelScope.launch {
            getFoodById(foodId)
                .collect { result ->
                    when (result) {
                        is Resource.Loading -> {
                            _state.update {
                                it.copy(
                                    isDetailLoading = true,
                                    detailError = null,
                                    selectedServing = it.selectedServing,
                                    quantity = it.quantity,
                                    isManuallySelected = manuallySelected,
                                )
                            }
                        }

                        is Resource.Success -> {
                            val food = result.data ?: return@collect

                            _state.update {
                                it.copy(
                                    selectedFood = food,
                                    selectedServing = food.defaultServing,
                                    quantity = 1f,
                                    isDetailLoading = false,
                                    destination = if (manuallySelected) FoodBrowseDestination.Detail(food.id) else it.destination,
                                )
                            }
                        }

                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    detailError = result.message,
                                    isDetailLoading = false,
                                )
                            }
                        }
                    }
                }
        }
    }

    fun logFoodClicked() {
        val detail = foodDetailUiState.value as? FoodDetailUiState.Success ?: return

        viewModelScope.launch {
            _state.update { it.copy(isLogging = true) }

            try {
                val request = LogFoodRequest(
                    foodName = detail.foodName,
                    servingDescription = detail.selectedServing.description,
                    quantity = detail.quantity,
                    nutrition = detail.scaledNutrition,
                )

                when (val result = foodLog(request)) {
                    is Resource.Success -> {
                        showToast(
                            message = "Food logged successfully.",
                            type = SuccessToast(),
                        )
                        _events.emit(FoodBrowseEvent.FoodLogged)
                    }

                    is Resource.Error -> {
                        showToast(
                            message = result.message ?: "Unable to log food.",
                            type = ErrorToast(),
                        )
                    }

                    else -> Unit
                }
            } finally {
                _state.update { it.copy(isLogging = false) }
            }
        }
    }

    fun onSaveToggleClicked() {
        val food = _state.value.selectedFood ?: return
        val isSaved = (foodDetailUiState.value as? FoodDetailUiState.Success)?.isSaved ?: false

        viewModelScope.launch {
            if (isSaved) {
                removeSavedFood(food.id)
            } else {
                saveFood(food)
            }
        }
    }

    private fun showToast(
        message: String,
        type: CustomToastProperty = SuccessToast(),
        duration: Long = 3000L,
    ) {
        _state.update {
            it.copy(
                toast = ToastState(
                    visible = true,
                    message = message,
                    type = type,
                    duration = duration,
                ),
            )
        }
    }

    fun hideToast() {
        _state.update { it.copy(toast = ToastState()) }
    }
}
