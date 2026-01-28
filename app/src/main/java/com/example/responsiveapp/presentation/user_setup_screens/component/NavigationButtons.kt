package com.example.responsiveapp.presentation.user_setup_screens.component
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NavigationButtons(
    canProceed: Boolean,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onNextClick,
        enabled = canProceed,
        modifier = modifier,
        shape = MaterialTheme.shapes.medium
    ) {
        Text("Continue")
        Spacer(modifier = Modifier.width(8.dp))
        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
    }
}