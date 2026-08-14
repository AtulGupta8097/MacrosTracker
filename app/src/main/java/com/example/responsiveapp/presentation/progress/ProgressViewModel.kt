package com.example.responsiveapp.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.core.utils.DateUtils
import com.example.responsiveapp.domain.use_case.dailysummary.ObserveDailySummariesBetweenDatesUseCase
import com.example.responsiveapp.presentation.progress.mapper.toMacroDistributionUiState
import com.example.responsiveapp.presentation.progress.mapper.toNutritionOverviewUiState
import com.example.responsiveapp.presentation.progress.model.MacroType
import com.example.responsiveapp.presentation.progress.model.ProgressRange
import com.example.responsiveapp.presentation.progress.model.ProgressUiState
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
import javax.inject.Inject

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val observeDailySummariesBetweenDatesUseCase:
    ObserveDailySummariesBetweenDatesUseCase,
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

        val selectedMacroFlow = _state
            .map { it.selectedMacro }
            .distinctUntilChanged()

        combine(
            rangeFlow,
            selectedMacroFlow,
            summariesFlow,
        ) { range, selectedMacro, summaries ->

            summaries.toNutritionOverviewUiState(
                range = range,
                selectedMacro = selectedMacro,
            )
        }
            .onEach { nutritionOverview ->

                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        nutritionOverview = nutritionOverview,
                        macroDistribution = nutritionOverview.average.toMacroDistributionUiState(),
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}