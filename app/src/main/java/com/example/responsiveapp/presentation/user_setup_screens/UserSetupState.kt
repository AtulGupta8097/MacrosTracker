package com.example.responsiveapp.presentation.user_setup_screens

import com.example.responsiveapp.domain.model.UserData

data class UserSetupState(
    val currentScreen: UserSetupScreen = UserSetupScreen.Gender,
    val userData: UserData = UserData()
)