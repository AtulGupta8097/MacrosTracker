package com.example.responsiveapp.presentation.settings.componet

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AccountSection(
    onChangePasswordClick: () -> Unit,
    onDeleteAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SettingsSectionCard(modifier = modifier, label = "Account") {

        SettingsRow(
            title = "Change Password",
            icon = Icons.Default.Lock,
            iconTint = MaterialTheme.colorScheme.secondaryContainer,
            iconBackground = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.14f),
            showChevron = true,
            onClick = onChangePasswordClick,
        )

        HorizontalDivider(
            modifier = Modifier,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
        )

        SettingsRow(
            title = "Delete Account",
            titleColor = MaterialTheme.colorScheme.error,
            icon = Icons.Default.DeleteForever,
            iconTint = MaterialTheme.colorScheme.error,
            iconBackground = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
            onClick = onDeleteAccountClick,
        )
    }
}
