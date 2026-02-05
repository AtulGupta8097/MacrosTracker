package com.example.responsiveapp.presentation.user_setup_screens.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.responsiveapp.presentation.user_setup_screens.UserSetupScreen
import com.example.responsiveapp.presentation.user_setup_screens.UserSetupViewModel
import com.example.responsiveapp.presentation.user_setup_screens.component.NavigationButtons
import com.example.responsiveapp.presentation.user_setup_screens.component.TopBar

private val SCREEN_FLOW = listOf(
    UserSetupScreen.Gender,
    UserSetupScreen.Age,
    UserSetupScreen.Height,
    UserSetupScreen.Weight,
    UserSetupScreen.Activity,
    UserSetupScreen.Goal
)

@Composable
fun UserSetupMainScreen(
    viewModel: UserSetupViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val currentIndex = SCREEN_FLOW.indexOf(state.currentScreen)
    val progress =
        if (currentIndex >= 0) (currentIndex + 1).toFloat() / SCREEN_FLOW.size else 1f

    Scaffold(
        topBar = {
            if (state.currentScreen !is UserSetupScreen.Complete) {
                TopBar(
                    title = "Setup Profile",
                    showBackButton = currentIndex > 0,
                    onBackClick = viewModel::previousScreen,
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
//                        .background(MaterialTheme.colorScheme.secondary)
                        .windowInsetsPadding(
                            WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
                        )
                )
            }
        },
        bottomBar = {
            if (state.currentScreen !is UserSetupScreen.Complete) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .navigationBarsPadding(),
                    color = MaterialTheme.colorScheme.background,
                    shadowElevation = 12.dp,
                    tonalElevation = 3.dp
                ) {
                    NavigationButtons(
                        canProceed = viewModel.canProceed(),
                        onNextClick = viewModel::onNextClicked,
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 16.dp
                        ).size(56.dp)
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .windowInsetsPadding(
                    WindowInsets.systemBars.only(WindowInsetsSides.Horizontal)
                ) // Prevent content from going under side navigation bars in landscape
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            when (state.currentScreen) {
                is UserSetupScreen.Gender -> GenderScreen(
                    selectedGender = state.userInput.gender,
                    onGenderSelected = viewModel::updateGender
                )
                is UserSetupScreen.Age -> {
                    AgeScreen(
                        age = state.userInput.age,
                        onAgeChanged = viewModel::updateAge
                    )
                }
                is UserSetupScreen.Height -> {
                    HeightScreen(
                        height = state.userInput.height,
                        onHeightChanged = viewModel::updateHeight
                    )
                }
                is UserSetupScreen.Weight -> {
                    WeightScreen(
                        weight = state.userInput.weight,
                        onWeightChanged = viewModel::updateWeight
                    )
                }
                is UserSetupScreen.Activity -> {
                    ActivityLevelScreen(
                        selectedActivityLevel = state.userInput.activityLevel,
                        onActivityLevelSelected = viewModel::updateActivityLevel
                    )
                }
                is UserSetupScreen.Goal -> {
                    GoalScreen(
                        selectedGoal = state.userInput.goal,
                        onGoalSelected = viewModel::updateGoal
                    )
                }
                is UserSetupScreen.Complete -> {
                    UserSetupCompleteScreen(
                        isSaving = state.isSaving,
                        macros = state.macros,
                        onNavigateToMain = {}
                    )
                }

                else -> Unit
            }
        }
    }
}