package com.example.responsiveapp.presentation.user_setup_screens

sealed class UserSetupScreen {
    object Gender : UserSetupScreen()
    object Age : UserSetupScreen()
    object Weight : UserSetupScreen()
    object Height : UserSetupScreen()
    object Activity : UserSetupScreen()
    object Goal : UserSetupScreen()
    object Complete : UserSetupScreen()
}