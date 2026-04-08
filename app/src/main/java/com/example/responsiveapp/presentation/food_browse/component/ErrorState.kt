package com.example.responsiveapp.presentation.food_browse.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun ErrorState(error: String, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.statusBarsPadding().padding(MaterialTheme.spacing.md)) {
        IconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.lg),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.sm)
        ) {
            Text("Couldn't load food", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold))
            Text(error, style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurfaceVariant))
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}