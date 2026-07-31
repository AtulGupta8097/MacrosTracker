package com.example.responsiveapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.core.utils.DateUtils
import com.example.responsiveapp.domain.use_case.dailysummary.ObserveDailySummaryForDateUseCase
import com.example.responsiveapp.domain.use_case.foodlog.ObserveFoodLogsForDateUseCase
import com.example.responsiveapp.domain.use_case.macrostarget.GetCurrentMacroTargetUseCase
import com.example.responsiveapp.domain.use_case.profile.ObserveUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    private val _state = MutableStateFlow(
        HomeUiState(
            selectedDate = today,
            weekStartDate = DateUtils.getWeekStart(today),
            weekDays = DateUtils.getCurrentWeekDates(
                DateUtils.getWeekStart(today)
            ),
        )
    )
    val state = _state.asStateFlow()

    /**
     * Every feature that depends on the selected date
     * should observe this flow.
     */
    private val selectedDateFlow =
        state
            .map { it.selectedDate }
            .distinctUntilChanged()

    init {
        observeUserProfile()
        observeDailySummary()
        observeFoodLogs()
        loadMacroTarget()
    }

    private fun observeUserProfile() {
        observeUserProfileUseCase()
            .onEach { profile ->
                _state.update {
                    it.copy(
                        userProfile = profile,
                        isLoading = false,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeDailySummary() {
        selectedDateFlow
            .flatMapLatest(observeDailySummaryForDateUseCase::invoke)
            .onEach { summary ->
                _state.update {
                    it.copy(
                        dailySummary = summary
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeFoodLogs() {
        selectedDateFlow
            .flatMapLatest { date ->
                observeFoodLogsForDateUseCase(date)
            }
            .onEach { foodLogs ->
                _state.update { state ->
                    state.copy(
                        foodLogs = foodLogs
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadMacroTarget() {
        viewModelScope.launch {
            val target = getCurrentMacroTargetUseCase()

            _state.update { state ->
                state.copy(
                    macroTarget = target,
                )
            }
        }
    }

    fun onDateSelected(date: Long) {
        _state.update { current ->

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
        _state.update { current ->
            current.copyForWeek(
                DateUtils.getPreviousWeek(current.weekStartDate)
            )
        }
    }

    fun onNextWeek() {
        _state.update { current ->

            if (DateUtils.isCurrentWeek(current.weekStartDate)) {
                return@update current
            }

            current.copyForWeek(
                DateUtils.getNextWeek(current.weekStartDate)
            )
        }
    }

    fun onGoToToday() {
        val currentWeekStart = DateUtils.getWeekStart(today)

        _state.update {
            it.copy(
                selectedDate = today,
                weekStartDate = currentWeekStart,
                weekDays = DateUtils.getCurrentWeekDates(currentWeekStart),
            )
        }
    }

    /**
     * Creates a new state for another week while preserving
     * the currently selected weekday.
     */
    private fun HomeUiState.copyForWeek(
        newWeekStart: Long,
    ): HomeUiState {

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