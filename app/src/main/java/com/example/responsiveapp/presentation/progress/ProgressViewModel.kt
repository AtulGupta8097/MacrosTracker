package com.example.responsiveapp.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.core.utils.DateUtils
import com.example.responsiveapp.domain.use_case.dailysummary.ObserveDailySummariesBetweenDatesUseCase
import com.example.responsiveapp.domain.use_case.weight.ObserveWeightHistoryUseCase
import com.example.responsiveapp.domain.use_case.weight.SaveWeightUseCase
import com.example.responsiveapp.presentation.progress.mapper.toMacroDistributionUiState
import com.example.responsiveapp.presentation.progress.mapper.toNutritionOverviewUiState
import com.example.responsiveapp.presentation.progress.mapper.toWeightProgressUiState
import com.example.responsiveapp.presentation.progress.model.MacroDistributionUiState
import com.example.responsiveapp.presentation.progress.model.MacroType
import com.example.responsiveapp.presentation.progress.model.NutritionOverviewUiState
import com.example.responsiveapp.presentation.progress.model.ProgressRange
import com.example.responsiveapp.presentation.progress.model.ProgressUiState
import com.example.responsiveapp.presentation.progress.model.WeightProgressUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val observeDailySummariesBetweenDatesUseCase: ObserveDailySummariesBetweenDatesUseCase,
    private val observeWeightHistoryUseCase: ObserveWeightHistoryUseCase,
    private val saveWeightUseCase: SaveWeightUseCase,
) : ViewModel() {

    private val today = DateUtils.today()

    private val _state =
        MutableStateFlow(ProgressUiState())

    val state = _state.asStateFlow()

    init {
        observeProgressState()
    }

    fun onRangeSelected(range: ProgressRange) {
        _state.update {
            it.copy(range = range)
        }
    }

    fun onMacroSelected(macroType: MacroType) {
        _state.update { currentState ->
            currentState.copy(
                selectedMacro = macroType,
            )
        }
    }

    fun onLogWeightClicked() {
        _state.update { it.copy(isLogWeightDialogVisible = true) }
    }

    fun onDismissLogWeightDialog() {
        _state.update { it.copy(isLogWeightDialogVisible = false) }
    }

    fun onLogWeight(weight: Float) {
        viewModelScope.launch {
            saveWeightUseCase(weight = weight, date = today)
            _state.update { it.copy(isLogWeightDialogVisible = false) }
        }
    }

    private fun observeProgressState() {

        val rangeFlow = _state
            .map { it.range }
            .distinctUntilChanged()

        val summariesFlow = rangeFlow
            .flatMapLatest { range ->

                val startDate = DateUtils.minusDays(
                    today,
                    (range.days - 1).toLong(),
                )

                observeDailySummariesBetweenDatesUseCase(
                    startDate = startDate,
                    endDate = today,
                )
            }

        val weightLogsFlow = observeWeightHistoryUseCase()

        val selectedMacroFlow = _state
            .map { it.selectedMacro }
            .distinctUntilChanged()

        combine(
            rangeFlow,
            selectedMacroFlow,
            summariesFlow,
            weightLogsFlow,
        ) { range, selectedMacro, summaries, weightLogs ->

            val nutritionOverview = summaries.toNutritionOverviewUiState(
                range = range,
                selectedMacro = selectedMacro,
            )

            ProgressSnapshot(
                nutritionOverview = nutritionOverview,
                macroDistribution = nutritionOverview.average.toMacroDistributionUiState(),
                weightProgress = weightLogs.toWeightProgressUiState(),
            )
        }
            .onEach { snapshot ->

                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        nutritionOverview = snapshot.nutritionOverview,
                        macroDistribution = snapshot.macroDistribution,
                        weightProgress = snapshot.weightProgress,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private data class ProgressSnapshot(
        val nutritionOverview: NutritionOverviewUiState,
        val macroDistribution: MacroDistributionUiState,
        val weightProgress: WeightProgressUiState,
    )
}