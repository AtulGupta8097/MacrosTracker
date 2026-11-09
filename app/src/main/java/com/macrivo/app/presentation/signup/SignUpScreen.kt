package com.macrivo.app.presentation.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.macrivo.app.presentation.auth.AuthValidator
import com.macrivo.app.presentation.auth.component.AuthField
import com.macrivo.app.presentation.auth.component.AuthPrimaryButton
import com.macrivo.app.presentation.auth.component.AuthScreenLayout
import com.macrivo.app.presentation.auth.component.GoogleSignInButton
import com.macrivo.app.presentation.auth.component.OrDivider
import com.macrivo.app.presentation.auth.rememberGoogleIdTokenProvider
import com.macrivo.app.presentation.commoncomponent.CustomToast
import com.macrivo.app.presentation.commoncomponent.ErrorToast
import com.macrivo.app.presentation.commoncomponent.HighLightedText
import com.macrivo.app.presentation.ui.theme.spacing

@Composable
fun SignUpScreen(
    viewmodel: SignUpViewmodel = hiltViewModel(),
    navigateToSignIn: () -> Unit,
    navigateToHome: () -> Unit,
    navigateToUserSetup: () -> Unit
) {
    val uiState by viewmodel.uiState.collectAsState()
    val googleTokenProvider = rememberGoogleIdTokenProvider()
    val toastType = remember { ErrorToast() }
    var showToast by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewmodel.event.collect { event ->
            when (event) {
                is SignUpEvent.NavigateToHome -> navigateToHome()
                is SignUpEvent.NavigateToLogin -> navigateToSignIn()
                is SignUpEvent.NavigateToUserSetup -> navigateToUserSetup()
                is SignUpEvent.ShowError -> {
                    message = event.message
                    showToast = true
                }
            }
        }
    }

    AuthScreenLayout(
        heading = "Create account",
        subHeading = "Set your goals and start logging meals",
        overlay = {
            CustomToast(
                message = message,
                type = toastType,
                visibility = showToast,
                onDismiss = { showToast = false }
            )
        }
    ) {
        AuthField(
            label = "Name",
            value = uiState.username,
            onValueChange = viewmodel::onUsernameChange,
            placeholder = "Your name",
            icon = Icons.Filled.Person,
            error = uiState.usernameError,
            enabled = !uiState.isBusy,
            imeAction = ImeAction.Next
        )

        AuthField(
            label = "Email",
            value = uiState.email,
            onValueChange = viewmodel::onEmailChange,
            placeholder = "name@example.com",
            icon = Icons.Filled.Mail,
            error = uiState.emailError,
            enabled = !uiState.isBusy,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        )

        Column(verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)) {
            AuthField(
                label = "Password",
                value = uiState.password,
                onValueChange = viewmodel::onPasswordChange,
                placeholder = "Create a password",
                icon = Icons.Filled.Lock,
                error = uiState.passwordError,
                enabled = !uiState.isBusy,
                imeAction = ImeAction.Next,
                isPassword = true,
                passwordVisible = uiState.isPasswordVisible,
                onTogglePasswordVisibility = viewmodel::togglePasswordVisibility
            )

            if (uiState.passwordError == null) {
                Text(
                    text = "At least ${AuthValidator.MIN_PASSWORD_LENGTH} characters with a letter and a number",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = MaterialTheme.spacing.xs)
                )
            }
        }

        AuthField(
            label = "Confirm password",
            value = uiState.confirmPassword,
            onValueChange = viewmodel::onConfirmPasswordChange,
            placeholder = "Re-enter your password",
            icon = Icons.Filled.Lock,
            error = uiState.confirmPasswordError,
            enabled = !uiState.isBusy,
            imeAction = ImeAction.Done,
            onImeAction = viewmodel::signUp,
            isPassword = true,
            passwordVisible = uiState.isConfirmPasswordVisible,
            onTogglePasswordVisibility = viewmodel::toggleConfirmPasswordVisibility
        )

        AuthPrimaryButton(
            text = "Create account",
            onClick = viewmodel::signUp,
            isLoading = uiState.isLoading,
            enabled = !uiState.isGoogleLoading
        )

        OrDivider()

        GoogleSignInButton(
            text = "Sign up with Google",
            onClick = { viewmodel.onGoogleSignInClicked(googleTokenProvider::getIdToken) },
            enabled = !uiState.isLoading,
            isLoading = uiState.isGoogleLoading
        )

        HighLightedText(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = MaterialTheme.spacing.sm),
            normal = "Already have an account? ",
            highlighted = "Sign in",
            normalStyle = SpanStyle(color = MaterialTheme.colorScheme.onBackground),
            highlightedStyle = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
            onClick = viewmodel::onSignInClicked
        )
    }
}
