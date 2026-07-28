package com.example.responsiveapp.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.responsiveapp.presentation.home.componet.HomeAppBar
import com.example.responsiveapp.presentation.home.componet.WeeklyDatePicker
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun HomeScreen(
    onEditProfile: () -> Unit = {},
    onSeeMoreProfile: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.spacing.md
            )
        ) {

            HomeAppBar(
                userProfile = state.userProfile,
                onEditProfile = onEditProfile,
                onSeeMoreProfile = onSeeMoreProfile
            )

            WeeklyDatePicker(
                weekStartDate = state.weekStartDate,
                weekDays = state.weekDays,
                selectedDate = state.selectedDate,
                onDateSelected = viewModel::onDateSelected,
                onPreviousWeek = viewModel::onPreviousWeek,
                onNextWeek = viewModel::onNextWeek,
                onGoToToday = viewModel::onGoToToday,
            )
        }
    }
}