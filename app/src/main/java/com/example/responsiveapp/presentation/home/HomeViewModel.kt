package com.example.responsiveapp.presentation.home

import androidx.lifecycle.ViewModel
import com.example.responsiveapp.domain.session.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sessionManager: SessionManager
): ViewModel() {


}