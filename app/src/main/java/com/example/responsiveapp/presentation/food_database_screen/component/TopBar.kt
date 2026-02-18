package com.example.responsiveapp.presentation.food_database_screen.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun TopBar(onBack: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = MaterialTheme.spacing.md, MaterialTheme.spacing.md),
        verticalAlignment = Alignment.CenterVertically
    ) {
            Icon(
                modifier = Modifier.clickable(
                    onClick = onBack
                )
                    .align(Alignment.CenterVertically),
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground,
            )


        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Text(
                text = "Log food",
                style = MaterialTheme.typography.titleLarge
                    .copy(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    ),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}