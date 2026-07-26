package com.example.responsiveapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.responsiveapp.domain.use_case.profile.ObserveUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val observeUserProfileUseCase: ObserveUserProfileUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        observeUserProfile()
    }

    private fun observeUserProfile() {
        observeUserProfileUseCase()
            .onEach { profile ->
                _state.update {
                    it.copy(
                        userProfile = profile,
                        isLoading = false
                    )
                }
            }
            .launchIn(viewModelScope)
    }
}