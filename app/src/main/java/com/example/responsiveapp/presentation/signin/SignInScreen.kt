package com.example.responsiveapp.presentation.signin

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.responsiveapp.presentation.auth.component.AuthField
import com.example.responsiveapp.presentation.auth.component.AuthPrimaryButton
import com.example.responsiveapp.presentation.auth.component.AuthScreenLayout
import com.example.responsiveapp.presentation.auth.component.GoogleSignInButton
import com.example.responsiveapp.presentation.auth.component.OrDivider
import com.example.responsiveapp.presentation.auth.rememberGoogleIdTokenProvider
import com.example.responsiveapp.presentation.commoncomponent.CustomToast
import com.example.responsiveapp.presentation.commoncomponent.ErrorToast
import com.example.responsiveapp.presentation.commoncomponent.HighLightedText
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun SignInScreen(
    viewmodel: SignInViewModel = hiltViewModel(),
    navigateToSignUp: () -> Unit,
    navigateToForgotPassword: (String) -> Unit,
    navigateToMainScreen: () -> Unit,
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
                SignInEvent.NavigateToMainScreen -> navigateToMainScreen()
                SignInEvent.NavigateToUserSetup -> navigateToUserSetup()
                SignInEvent.NavigateToSignUp -> navigateToSignUp()
                is SignInEvent.NavigateToForgotPassword -> navigateToForgotPassword(event.email)
                is SignInEvent.ShowError -> {
                    message = event.message
                    showToast = true
                }
            }
        }
    }

    AuthScreenLayout(
        heading = "Welcome back",
        subHeading = "Sign in to keep tracking your macros",
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

        AuthField(
            label = "Password",
            value = uiState.password,
            onValueChange = viewmodel::onPasswordChange,
            placeholder = "Enter your password",
            icon = Icons.Filled.Lock,
            error = uiState.passwordError,
            enabled = !uiState.isBusy,
            imeAction = ImeAction.Done,
            onImeAction = viewmodel::signIn,
            isPassword = true,
            passwordVisible = uiState.isPasswordVisible,
            onTogglePasswordVisibility = viewmodel::togglePasswordVisibility
        )

        HighLightedText(
            modifier = Modifier
                .align(Alignment.End)
                .padding(vertical = MaterialTheme.spacing.xs),
            highlighted = "Forgot password?",
            highlightedStyle = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
            onClick = viewmodel::onForgotPasswordClicked
        )

        AuthPrimaryButton(
            text = "Sign in",
            onClick = viewmodel::signIn,
            isLoading = uiState.isLoading,
            enabled = !uiState.isGoogleLoading
        )

        OrDivider()

        GoogleSignInButton(
            text = "Continue with Google",
            onClick = { viewmodel.onGoogleSignInClicked(googleTokenProvider::getIdToken) },
            enabled = !uiState.isLoading,
            isLoading = uiState.isGoogleLoading
        )

        HighLightedText(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(vertical = MaterialTheme.spacing.sm),
            normal = "Don't have an account? ",
            highlighted = "Sign up",
            normalStyle = SpanStyle(color = MaterialTheme.colorScheme.onBackground),
            highlightedStyle = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            ),
            textStyle = MaterialTheme.typography.bodyMedium,
            onClick = viewmodel::onSignUpClicked
        )
    }
}
