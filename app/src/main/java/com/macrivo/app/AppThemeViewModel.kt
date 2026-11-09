package com.macrivo.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.macrivo.app.data.datastore.AppPrefManager
import com.macrivo.app.domain.model.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppThemeViewModel @Inject constructor(
    appPrefManager: AppPrefManager,
) : ViewModel() {

    val themeMode = appPrefManager.themeMode.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ThemeMode.SYSTEM,
    )
}
