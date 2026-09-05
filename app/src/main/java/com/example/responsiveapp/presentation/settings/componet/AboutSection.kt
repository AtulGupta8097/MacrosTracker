package com.example.responsiveapp.presentation.settings.componet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun AboutSection(
    versionText: String,
    onPrivacyPolicyClick: () -> Unit,
    onTermsClick: () -> Unit,
    onRateAppClick: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {

        SettingsSectionCard(label = "About") {
            SettingsRow(
                title = "Privacy Policy",
                onClick = onPrivacyPolicyClick,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            SettingsRow(
                title = "Terms of Service",
                onClick = onTermsClick,
            )
            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))
            SettingsRow(
                title = "Rate the App",
                onClick = onRateAppClick,
            )
        }

        OutlinedButton(
            onClick = onSignOutClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.spacing.lg),
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.error),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error,
            ),
            shape = MaterialTheme.shapes.medium,
        ) {
            Text(
                text = "Sign Out",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
            )
        }

        Text(
            text = versionText,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.spacing.sm),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
