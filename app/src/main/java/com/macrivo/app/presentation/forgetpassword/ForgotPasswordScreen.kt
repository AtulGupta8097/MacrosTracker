package com.macrivo.app.presentation.forgetpassword

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.macrivo.app.presentation.auth.component.AuthField
import com.macrivo.app.presentation.auth.component.AuthPrimaryButton
import com.macrivo.app.presentation.auth.component.AuthScreenLayout
import com.macrivo.app.presentation.commoncomponent.HighLightedText
import com.macrivo.app.presentation.ui.theme.spacing

@Composable
fun ForgotPasswordScreen(
    initialEmail: String,
    onBackToSignIn: () -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(initialEmail) { viewModel.prefill(initialEmail) }

    AuthScreenLayout(
        heading = "Reset your password",
        subHeading = "We'll email you a link to choose a new one",
    ) {
        val sentTo = state.sentTo

        if (sentTo == null) {
            Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(MaterialTheme.spacing.md)) {
                AuthField(
                    label = "Email address",
                    value = state.email,
                    onValueChange = viewModel::onEmailChange,
                    placeholder = "name@example.com",
                    icon = Icons.Filled.Mail,
                    error = state.emailError,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done,
                    onImeAction = viewModel::onSendClicked,
                )

                state.errorMessage?.let { message ->
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }

                AuthPrimaryButton(
                    text = "Send reset link",
                    onClick = viewModel::onSendClicked,
                    isLoading = state.isLoading,
                )
            }
        } else {
            Text(
                text = "If an account exists for $sentTo, we've sent a link to reset your " +
                    "password. It may take a few minutes - check your spam folder too.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        RoundedCornerShape(12.dp),
                    )
                    .padding(MaterialTheme.spacing.md),
            )

            AuthPrimaryButton(
                text = "Back to sign in",
                onClick = onBackToSignIn,
                imageVector = null,
            )

            HighLightedText(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                normal = "Wrong address? ",
                highlighted = "Use a different email",
                normalStyle = SpanStyle(color = MaterialTheme.colorScheme.onBackground),
                highlightedStyle = SpanStyle(color = MaterialTheme.colorScheme.primary),
                textStyle = MaterialTheme.typography.bodyMedium,
                onClick = viewModel::onUseDifferentEmail,
            )
        }

        if (sentTo == null) {
            HighLightedText(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                normal = "Remembered it? ",
                highlighted = "Back to sign in",
                normalStyle = SpanStyle(color = MaterialTheme.colorScheme.onBackground),
                highlightedStyle = SpanStyle(color = MaterialTheme.colorScheme.primary),
                textStyle = MaterialTheme.typography.bodyMedium,
                onClick = { if (!state.isLoading) onBackToSignIn() },
            )
        }
    }
}
