package com.example.responsiveapp.presentation.user_setup_screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.data.datastore.AppPrefManager
import com.example.responsiveapp.domain.model.ActivityLevel
import com.example.responsiveapp.domain.model.Gender
import com.example.responsiveapp.domain.model.Goal
import com.example.responsiveapp.domain.use_case.CalculateMacrosUseCase
import com.example.responsiveapp.domain.use_case.profile_usecase.SaveUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class UserSetupViewModel @Inject constructor(
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
    private val calculateMacrosUseCase: CalculateMacrosUseCase,
    private val appPrefManager: AppPrefManager
): ViewModel() {
    private val _state = MutableStateFlow(UserSetupState())
    val state: StateFlow<UserSetupState> = _state.asStateFlow()
    private val screenFlow = listOf(
        UserSetupScreen.Gender,
        UserSetupScreen.Age,
        UserSetupScreen.Height,
        UserSetupScreen.Weight,
        UserSetupScreen.Activity,
        UserSetupScreen.Goal,
        UserSetupScreen.Complete
    )

    fun updateGender(gender: Gender) {
        _state.update { it.copy(userInput = it.userInput.copy(gender = gender)) }
    }

    fun updateAge(age: Int?) {
        _state.update { it.copy(userInput = it.userInput.copy(age = age)) }
    }

    fun updateWeight(weight: Float?) {
        _state.update { it.copy(userInput = it.userInput.copy(weight = weight)) }
    }

    fun updateHeight(height: Float?) {
        _state.update { it.copy(userInput = it.userInput.copy(height = height)) }
    }

    fun updateActivityLevel(level: ActivityLevel) {
        _state.update { it.copy(userInput = it.userInput.copy(activityLevel = level)) }
    }

    fun updateGoal(goal: Goal) {
        _state.update { it.copy(userInput = it.userInput.copy(goal = goal)) }
    }
    fun onNextClicked() {
        if (!(_state.value.currentScreen == UserSetupScreen.Goal)) {
            nextScreen()
        } else {
            onCompleteUserSetup()
        }
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
            is UserSetupScreen.Gender -> _state.value.userInput.gender != null
            is UserSetupScreen.Age -> _state.value.userInput.age != null
            is UserSetupScreen.Height -> _state.value.userInput.height != null
            is UserSetupScreen.Weight -> _state.value.userInput.weight != null
            is UserSetupScreen.Activity -> _state.value.userInput.activityLevel != null
            is UserSetupScreen.Goal -> _state.value.userInput.goal != null
            else -> false
        }
    }
    fun onCompleteUserSetup() {
        _state.update {
            it.copy(
                currentScreen = UserSetupScreen.Complete,
                isSaving = true,
                error = null)
        }
        val profile = _state.value.userInput.toDomain()

        viewModelScope.launch {

            val result = saveUserProfileUseCase(profile)

            result.fold(
                onSuccess = {
                    appPrefManager.setUserSetup(true)
                    val macros = calculateMacrosUseCase(profile)
                    _state.update {
                        it.copy(
                            isSaving = false,
                            macros = macros
                        )
                    }
                },
                onFailure = { throwable ->
                    _state.update {
                        it.copy(
                            isSaving = false,
                            error = throwable.message,
                        )
                    }
                }
            )
        }
    }

}