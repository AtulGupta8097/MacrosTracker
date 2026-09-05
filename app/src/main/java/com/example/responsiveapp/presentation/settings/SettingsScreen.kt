package com.example.responsiveapp.presentation.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.responsiveapp.domain.model.ActivityLevel
import com.example.responsiveapp.domain.model.Goal
import com.example.responsiveapp.presentation.settings.componet.AboutSection
import com.example.responsiveapp.presentation.settings.componet.AccountSection
import com.example.responsiveapp.presentation.settings.componet.BodyGoalsSection
import com.example.responsiveapp.presentation.settings.componet.ProfileHeroCard
import com.example.responsiveapp.presentation.settings.componet.ThemeSection
import com.example.responsiveapp.presentation.settings.componet.sheet.EditProfileSheet
import com.example.responsiveapp.presentation.settings.componet.sheet.NumericInputSheet
import com.example.responsiveapp.presentation.settings.componet.sheet.OptionListSheet
import com.example.responsiveapp.presentation.settings.componet.sheet.SheetOption
import com.example.responsiveapp.presentation.ui.theme.spacing

private enum class SettingsSheet {
    EditProfile, Weight, TargetWeight, Goal, Activity
}

private enum class ConfirmDialog {
    DeleteAccount, SignOut
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onPrivacyPolicyClick: () -> Unit = {},
    onTermsClick: () -> Unit = {},
    onRateAppClick: () -> Unit = {},
    onSignedOut: () -> Unit = {},
    onAccountDeleted: () -> Unit = {},
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    var activeSheet by rememberSaveable { mutableStateOf<SettingsSheet?>(null) }
    var confirmDialog by remember { mutableStateOf<ConfirmDialog?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val snackbarHostState = remember { SnackbarHostState() }

    fun closeSheet() {
        activeSheet = null
    }

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is SettingsEvent.Message -> snackbarHostState.showSnackbar(event.text)
                is SettingsEvent.Error -> snackbarHostState.showSnackbar(event.text)
                SettingsEvent.SignedOut -> onSignedOut()
                SettingsEvent.AccountDeleted -> onAccountDeleted()
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
    ) { scaffoldPadding ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding),
            color = MaterialTheme.colorScheme.background,
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = MaterialTheme.spacing.md,
                    vertical = MaterialTheme.spacing.md,
                ),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
            ) {

                item {
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Manage your profile, goals & preferences",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }

                item {
                    ProfileHeroCard(
                        profile = state.profile,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                item {
                    BodyGoalsSection(
                        profile = state.profile,
                        onWeightClick = { activeSheet = SettingsSheet.Weight },
                        onTargetWeightClick = { activeSheet = SettingsSheet.TargetWeight },
                        onGoalClick = { activeSheet = SettingsSheet.Goal },
                        onActivityClick = { activeSheet = SettingsSheet.Activity },
                        onBodyDetailsClick = { activeSheet = SettingsSheet.EditProfile },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                item {
                    ThemeSection(
                        selected = state.themeMode,
                        onThemeSelected = viewModel::onThemeSelected,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                item {
                    AccountSection(
                        onChangePasswordClick = viewModel::onChangePasswordRequested,
                        onDeleteAccountClick = { confirmDialog = ConfirmDialog.DeleteAccount },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                item {
                    AboutSection(
                        versionText = "Macrivo v1.0.0",
                        onPrivacyPolicyClick = onPrivacyPolicyClick,
                        onTermsClick = onTermsClick,
                        onRateAppClick = onRateAppClick,
                        onSignOutClick = { confirmDialog = ConfirmDialog.SignOut },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = MaterialTheme.spacing.lg),
                    )
                }
            }
        }
    }

    when (activeSheet) {

        SettingsSheet.Weight -> NumericInputSheet(
            sheetState = sheetState,
            title = "Update Current Weight",
            initialValue = state.profile.rawWeight,
            unit = "kg",
            onSave = {
                viewModel.onWeightSaved(it)
                closeSheet()
            },
            onDismiss = ::closeSheet,
        )

        SettingsSheet.TargetWeight -> NumericInputSheet(
            sheetState = sheetState,
            title = "Update Target Weight",
            initialValue = state.profile.rawTargetWeight,
            unit = "kg",
            onSave = {
                viewModel.onTargetWeightSaved(it)
                closeSheet()
            },
            onDismiss = ::closeSheet,
        )

        SettingsSheet.Goal -> OptionListSheet(
            sheetState = sheetState,
            title = "Select Your Goal",
            options = remember {
                Goal.entries.map { SheetOption(it, it.label, it.description) }
            },
            selected = state.profile.goal,
            onSelect = {
                viewModel.onGoalSelected(it)
                closeSheet()
            },
            onDismiss = ::closeSheet,
        )

        SettingsSheet.Activity -> OptionListSheet(
            sheetState = sheetState,
            title = "Select Activity Level",
            options = remember {
                ActivityLevel.entries.map { SheetOption(it, it.label, it.description) }
            },
            selected = state.profile.activityLevel,
            onSelect = {
                viewModel.onActivityLevelSelected(it)
                closeSheet()
            },
            onDismiss = ::closeSheet,
        )

        SettingsSheet.EditProfile -> EditProfileSheet(
            sheetState = sheetState,
            profile = state.profile,
            onSave = { name, height, age, gender ->
                viewModel.onProfileDetailsSaved(name, height, age, gender)
                closeSheet()
            },
            onDismiss = ::closeSheet,
        )

        null -> Unit
    }

    when (confirmDialog) {

        ConfirmDialog.DeleteAccount -> AlertDialog(
            onDismissRequest = { confirmDialog = null },
            title = { Text("Delete your account?") },
            text = {
                Text(
                    "This permanently deletes your account and all of your " +
                            "locally stored data. This can't be undone."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmDialog = null
                        viewModel.onDeleteAccountConfirmed()
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDialog = null }) {
                    Text("Cancel")
                }
            },
        )

        ConfirmDialog.SignOut -> AlertDialog(
            onDismissRequest = { confirmDialog = null },
            title = { Text("Sign out?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmDialog = null
                        viewModel.onSignOutConfirmed()
                    }
                ) {
                    Text("Sign Out")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmDialog = null }) {
                    Text("Cancel")
                }
            },
        )

        null -> Unit
    }
}
