package com.example.responsiveapp.presentation.user_setup_screens

import androidx.lifecycle.ViewModel
import com.example.responsiveapp.domain.model.ActivityLevel
import com.example.responsiveapp.domain.model.Gender
import com.example.responsiveapp.domain.model.Goal
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class UserSetupViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(UserSetupState())
    val state: StateFlow<UserSetupState> = _state.asStateFlow()

    private val screenFlow = listOf(
        UserSetupScreen.Gender,
        UserSetupScreen.Age,
        UserSetupScreen.Weight,
        UserSetupScreen.Height,
        UserSetupScreen.Activity,
        UserSetupScreen.Goal,
        UserSetupScreen.Complete
    )

    fun updateGender(gender: Gender) {
        _state.update { it.copy(userData = it.userData.copy(gender = gender)) }
    }

    fun updateAge(age: Int?) {
        _state.update { it.copy(userData = it.userData.copy(age = age)) }
    }

    fun updateWeight(weight: Float?) {
        _state.update { it.copy(userData = it.userData.copy(weight = weight)) }
    }

    fun updateHeight(height: Float?) {
        _state.update { it.copy(userData = it.userData.copy(height = height)) }
    }

    fun updateActivityLevel(level: ActivityLevel) {
        _state.update { it.copy(userData = it.userData.copy(activityLevel = level)) }
    }

    fun updateGoal(goal: Goal) {
        _state.update { it.copy(userData = it.userData.copy(goal = goal)) }
    }

    fun nextScreen() {
        val currentIndex = screenFlow.indexOf(_state.value.currentScreen)
        if (currentIndex < screenFlow.size - 1) {
            val nextScreen = screenFlow[currentIndex + 1]
            _state.update { it.copy(currentScreen = nextScreen) }
        }
    }

    fun previousScreen() {
        val currentIndex = screenFlow.indexOf(_state.value.currentScreen)
        if (currentIndex > 0) {
            val prevScreen = screenFlow[currentIndex - 1]
            _state.update { it.copy(currentScreen = prevScreen) }
        }
    }

    fun canProceed(): Boolean {
        return when (_state.value.currentScreen) {
            is UserSetupScreen.Gender -> _state.value.userData.gender != null
            is UserSetupScreen.Age -> _state.value.userData.age != null
            is UserSetupScreen.Weight -> _state.value.userData.weight != null
            is UserSetupScreen.Height -> _state.value.userData.height != null
            is UserSetupScreen.Activity -> _state.value.userData.activityLevel != null
            is UserSetupScreen.Goal -> _state.value.userData.goal != null
            else -> false
        }
    }

    fun completeOnboarding() {
        // TODO: Save userData to DataStore/Room database
        // TODO: Navigate to main app
    }
}