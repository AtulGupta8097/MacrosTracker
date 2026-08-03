package com.example.responsiveapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.core.utils.DateUtils
import com.example.responsiveapp.data.mapper.DateSelectionState
import com.example.responsiveapp.data.mapper.toDatePickerUiState
import kotlinx.coroutines.flow.combine
import com.example.responsiveapp.domain.use_case.dailysummary.ObserveDailySummaryForDateUseCase
import com.example.responsiveapp.presentation.home.mapper.toHealthMetricsUiState
import com.example.responsiveapp.presentation.home.mapper.toNutritionUiState
import com.example.responsiveapp.presentation.home.mapper.toRecentMealsUiState
import com.example.responsiveapp.domain.use_case.foodlog.ObserveFoodLogsForDateUseCase
import com.example.responsiveapp.domain.use_case.macrostarget.GetCurrentMacroTargetUseCase
import com.example.responsiveapp.domain.use_case.profile.ObserveUserProfileUseCase
import com.example.responsiveapp.presentation.home.mapper.toAppBarUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeUserProfileUseCase: ObserveUserProfileUseCase,
    private val observeDailySummaryForDateUseCase: ObserveDailySummaryForDateUseCase,
    private val observeFoodLogsForDateUseCase: ObserveFoodLogsForDateUseCase,
    private val getCurrentMacroTargetUseCase: GetCurrentMacroTargetUseCase,
) : ViewModel() {

    private val today = DateUtils.today()

    private val _dateState = MutableStateFlow(
        DateSelectionState(
            selectedDate = today,
            weekStartDate = DateUtils.getWeekStart(today),
            weekDays = DateUtils.getCurrentWeekDates(
                DateUtils.getWeekStart(today)
            ),
        )
    )

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    /**
     * Every feature that depends on the selected date
     * should observe this flow.
     */
    private val selectedDateFlow =
        _dateState
            .map { it.selectedDate }
            .distinctUntilChanged()

    private val foodLogsFlow =
        selectedDateFlow.flatMapLatest(observeFoodLogsForDateUseCase::invoke)

    private val dailySummaryFlow =
        selectedDateFlow.flatMapLatest(observeDailySummaryForDateUseCase::invoke)

    private val macroTargetFlow =
        flow {
            emit(getCurrentMacroTargetUseCase())
        }

    init {
      observeHomeState()
    }

    private fun observeHomeState() {
        combine(
            _dateState,
            observeUserProfileUseCase(),
            foodLogsFlow,
            dailySummaryFlow,
            macroTargetFlow,
        ) { dateNav, profile, foodLogs, dailySummary, macroTarget ->

            HomeUiState(
                isLoading = false,
                appBar = profile.toAppBarUiState(),
                datePicker = dateNav.toDatePickerUiState(),
                nutrition = dailySummary.toNutritionUiState(),
                healthMetrics = macroTarget.toHealthMetricsUiState(),
                recentMeals = foodLogs.toRecentMealsUiState(),
            )
        }
            .onEach { newState ->
                _state.value = newState
            }
            .launchIn(viewModelScope)
    }



    fun onDateSelected(date: Long) {
        _dateState.update { current ->

            val weekStart =
                if (DateUtils.isDateInWeek(date, current.weekStartDate)) {
                    current.weekStartDate
                } else {
                    DateUtils.getWeekStart(date)
                }

            current.copy(
                selectedDate = date,
                weekStartDate = weekStart,
                weekDays = DateUtils.getCurrentWeekDates(weekStart),
            )
        }
    }

    fun onPreviousWeek() {
        _dateState.update { current ->
            current.copyForWeek(
                DateUtils.getPreviousWeek(current.weekStartDate)
            )
        }
    }

    fun onNextWeek() {
        _dateState.update { current ->

            if (DateUtils.isCurrentWeek(current.weekStartDate)) {
                return@update current
            }

            current.copyForWeek(
                DateUtils.getNextWeek(current.weekStartDate)
            )
        }
    }


    /**
     * Creates a new state for another week while preserving
     * the currently selected weekday.
     */
    private fun DateSelectionState.copyForWeek(
        newWeekStart: Long,
    ): DateSelectionState {

        val weekdayIndex =
            DateUtils.getSelectedWeekdayIndex(
                date = selectedDate,
                weekStart = weekStartDate,
            )

        val candidateDate =
            DateUtils.getDateForWeekday(
                weekStart = newWeekStart,
                weekdayIndex = weekdayIndex,
            )

        val resolvedDate =
            if (DateUtils.isFutureDate(candidateDate)) today else candidateDate

        return copy(
            selectedDate = resolvedDate,
            weekStartDate = newWeekStart,
            weekDays = DateUtils.getCurrentWeekDates(newWeekStart),
        )
    }
}