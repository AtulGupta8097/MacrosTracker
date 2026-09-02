package com.example.responsiveapp.presentation.settings


sealed interface SettingsEvent {
    data class Message(val text: String) : SettingsEvent
    data class Error(val text: String) : SettingsEvent
    data object SignedOut : SettingsEvent
    data object AccountDeleted : SettingsEvent
}
