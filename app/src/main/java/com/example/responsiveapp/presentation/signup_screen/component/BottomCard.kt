package com.example.responsiveapp.presentation.signup_screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import com.example.responsiveapp.presentation.common_component.HighLightedText
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun BottomCard(
    onSignUpClicked: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(MaterialTheme.spacing.md),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {

            HighLightedText(normal = "Already have account? ", highlighted = "Sign in",
                normalStyle = SpanStyle(color = MaterialTheme.colorScheme.onBackground),
                highlightedStyle = SpanStyle(color = MaterialTheme.colorScheme.primary),
                textStyle = MaterialTheme.typography.bodyMedium,
                onClick = onSignUpClicked
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xxl))

        }
    }
}