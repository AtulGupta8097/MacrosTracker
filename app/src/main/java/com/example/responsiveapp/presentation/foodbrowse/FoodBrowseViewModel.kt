package com.example.responsiveapp.presentation.foodbrowse

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.core.utils.Resource
import com.example.responsiveapp.domain.calculator.ToastState
import com.example.responsiveapp.domain.model.CustomToastProperty
import com.example.responsiveapp.domain.model.Serving
import com.example.responsiveapp.domain.model.foodlog.LogFoodRequest
import com.example.responsiveapp.domain.model.scale
import com.example.responsiveapp.domain.use_case.food.GetFoodByIdUseCase
import com.example.responsiveapp.domain.use_case.food.SearchFoodsUseCase
import com.example.responsiveapp.domain.use_case.foodlog.LogFoodUseCase
import com.example.responsiveapp.presentation.commoncomponent.ErrorToast
import com.example.responsiveapp.presentation.commoncomponent.SuccessToast
import com.example.responsiveapp.presentation.foodbrowse.component.FoodBrowseEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "FoodBrowseViewModel"

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
@HiltViewModel
class FoodBrowseViewModel @Inject constructor(
    private val searchFoods: SearchFoodsUseCase,
    private val getFoodById: GetFoodByIdUseCase,
    private val foodLog: LogFoodUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FoodBrowseState())
    val state: StateFlow<FoodBrowseState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<FoodBrowseEvent>()

    val events = _events.asSharedFlow()

    val foodDetailUiState: StateFlow<FoodDetailUiState> = _state
        .map { s ->
            when {
                s.isDetailLoading -> FoodDetailUiState.Loading

                s.detailError != null -> FoodDetailUiState.Error(s.detailError)

                s.selectedFood != null && s.selectedServing != null ->
                    FoodDetailUiState.Success(
                        foodName = s.selectedFood.name,
                        foodBrand = s.selectedFood.brand,
                        servings = s.selectedFood.servings,
                        selectedServing = s.selectedServing,
                        quantity = s.quantity,
                        scaledNutrition = s.selectedServing.nutrition.scale(s.quantity),
                    )

                else -> FoodDetailUiState.Loading
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
            .map { it.query }
            .distinctUntilChanged()
            .flatMapLatest { query ->
                if (query.isBlank()) {
                    flowOf(query)
                } else {
                    flow {
                        kotlinx.coroutines.delay(300)
                        emit(query)
                    }
                }
            }
            .onEach { query -> searchFood(query) }
            .launchIn(viewModelScope)
    }

    override fun onCleared() {
        Log.d(TAG, "onCleared")
        super.onCleared()
    }

    private var searchJob: Job? = null
    private var detailJob: Job? = null

    fun onQueryChange(query: String) {
        _state.update { it.copy(query = query) }
    }

    fun searchFood(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {

            searchFoods(query, 38)
                .collect { result ->

                    when (result) {

                        is Resource.Loading -> {
                            _state.update { it.copy(isLoading = true) }
                        }

                        is Resource.Success -> {
                            _state.update {
                                it.copy(
                                    foods = result.data ?: emptyList(),
                                    isLoading = false,
                                    error = null
                                )
                            }
                        }

                        is Resource.Error -> {
                            _state.update {
                                it.copy(
                                    error = result.message,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
        }
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
                                    isDetailLoading = false
                                )
                            }
                        }
                    }
                }
        }
    }

    fun logFoodClicked() {

        val detail =
            foodDetailUiState.value as? FoodDetailUiState.Success
                ?: return

        viewModelScope.launch {

            _state.update {
                it.copy(isLogging = true)
            }

            try {

                val request = LogFoodRequest(

                    foodName = detail.foodName,

                    servingDescription =
                        detail.selectedServing.description,

                    quantity = detail.quantity,

                    nutrition = detail.scaledNutrition

                )

                when (val result = foodLog(request)) {

                    is Resource.Success -> {

                        showToast(
                            message = "Food logged successfully.",
                            type = SuccessToast()
                        )

                        _events.emit(
                            FoodBrowseEvent.FoodLogged
                        )
                    }

                    is Resource.Error -> {

                        showToast(
                            message = result.message ?: "Unable to log food.",
                            type = ErrorToast()
                        )
                    }

                    else -> Unit
                }

            } finally {

                _state.update {
                    it.copy(isLogging = false)
                }
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
                )
            )
        }
    }

    fun hideToast() {
        _state.update {
            it.copy(
                toast = ToastState()
            )
        }
    }
}