package com.example.responsiveapp.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.data.datastore.AppPrefManager
import com.example.responsiveapp.domain.model.ActivityLevel
import com.example.responsiveapp.domain.model.Gender
import com.example.responsiveapp.domain.model.Goal
import com.example.responsiveapp.domain.model.ThemeMode
import com.example.responsiveapp.domain.model.UserProfile
import com.example.responsiveapp.domain.session.SessionManager
import com.example.responsiveapp.domain.use_case.authentication.DeleteAccountUseCase
import com.example.responsiveapp.domain.use_case.authentication.SendPasswordResetEmailUseCase
import com.example.responsiveapp.domain.use_case.authentication.SignOutUseCase
import com.example.responsiveapp.domain.use_case.profile.ObserveUserProfileUseCase
import com.example.responsiveapp.domain.use_case.profile.SaveUserProfileUseCase
import com.example.responsiveapp.presentation.settings.mapper.toProfileSummaryUiState
import com.example.responsiveapp.presentation.settings.model.SettingsUiState
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val observeUserProfileUseCase: ObserveUserProfileUseCase,
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
    private val sendPasswordResetEmailUseCase: SendPasswordResetEmailUseCase,
    private val deleteAccountUseCase: DeleteAccountUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val appPrefManager: AppPrefManager,
    private val sessionManager: SessionManager,
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsUiState())
    val state = _state.asStateFlow()

    private val _events = MutableSharedFlow<SettingsEvent>()
    val events = _events.asSharedFlow()


    private var latestProfile: UserProfile? = null

    init {
        observeSettingsState()
    }

    private fun observeSettingsState() {
        combine(
            observeUserProfileUseCase(),
            appPrefManager.themeMode,
        ) { profile, themeMode ->

            latestProfile = profile

            SettingsUiState(
                isLoading = false,
                profile = profile.toProfileSummaryUiState(sessionManager.currentUserEmail()),
                themeMode = themeMode,
            )
        }
            .onEach { newState -> _state.value = newState }
            .launchIn(viewModelScope)
    }

    fun onThemeSelected(mode: ThemeMode) {
        viewModelScope.launch {
            appPrefManager.setThemeMode(mode)
        }
    }

    fun onProfileDetailsSaved(name: String, heightCm: Float, age: Int, gender: Gender) =
        updateProfile {
            it.copy(name = name, height = heightCm, age = age, gender = gender)
        }

    fun onWeightSaved(weightKg: Float) =
        updateProfile { it.copy(weight = weightKg) }

    fun onTargetWeightSaved(targetWeightKg: Float) =
        updateProfile { it.copy(targetWeight = targetWeightKg) }

    fun onGoalSelected(goal: Goal) =
        updateProfile { it.copy(goal = goal) }

    fun onActivityLevelSelected(activityLevel: ActivityLevel) =
        updateProfile { it.copy(activityLevel = activityLevel) }

    private fun updateProfile(transform: (UserProfile) -> UserProfile) {
        val current = latestProfile ?: return
        viewModelScope.launch {
            saveUserProfileUseCase(transform(current))
        }
    }

    fun onChangePasswordRequested() {
        val email = sessionManager.currentUserEmail()

        if (email.isNullOrBlank()) {
            viewModelScope.launch {
                _events.emit(SettingsEvent.Error("No email on file for this account"))
            }
            return
        }

        viewModelScope.launch {
            sendPasswordResetEmailUseCase(email).collect { result ->
                result
                    .onSuccess {
                        _events.emit(SettingsEvent.Message("Password reset link sent to $email"))
                    }
                    .onFailure {
                        _events.emit(
                            SettingsEvent.Error(it.message ?: "Couldn't send reset email")
                        )
                    }
            }
        }
    }

    fun onDeleteAccountConfirmed() {
        viewModelScope.launch {
            deleteAccountUseCase().collect { result ->
                result
                    .onSuccess {
                        _events.emit(SettingsEvent.AccountDeleted)
                    }
                    .onFailure { error ->
                        val message = if (error is FirebaseAuthRecentLoginRequiredException) {
                            "Please sign in again before deleting your account"
                        } else {
                            error.message ?: "Couldn't delete account"
                        }
                        _events.emit(SettingsEvent.Error(message))
                    }
            }
        }
    }

    fun onSignOutConfirmed() {
        viewModelScope.launch {
            signOutUseCase()
            _events.emit(SettingsEvent.SignedOut)
        }
    }
}
