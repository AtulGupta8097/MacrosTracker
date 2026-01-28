package com.example.responsiveapp.presentation.signin_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.responsiveapp.domain.model.CustomToastProperty
import com.example.responsiveapp.presentation.common_component.AuthTextField
import com.example.responsiveapp.presentation.common_component.AuthTopScreen
import com.example.responsiveapp.presentation.common_component.CustomButton
import com.example.responsiveapp.presentation.common_component.CustomToast
import com.example.responsiveapp.presentation.common_component.ErrorToast
import com.example.responsiveapp.presentation.common_component.HighLightedText
import com.example.responsiveapp.presentation.signin_screen.component.BottomCard
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun SignInScreen(
    viewmodel: SignInViewModel = hiltViewModel(),
    navigateToSignUp: () -> Unit,
    navigateToMainScreen: () -> Unit
) {
    val signUpState by viewmodel.state.collectAsState()
    val uiState by viewmodel.uiState.collectAsState()
    val scroll = rememberScrollState()
    var showToast by remember { mutableStateOf(false) }
    val toastType by remember { mutableStateOf<CustomToastProperty>(ErrorToast()) }
    var message by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {

        viewmodel.event.collect { event ->
            when (event) {
                SignInEvent.NavigateToMainScreen -> {
                    navigateToMainScreen.invoke()
                }
                SignInEvent.NavigateToSignUp -> {
                   navigateToSignUp.invoke()
                }
                is SignInEvent.ShowError -> {
                    showToast = true
                    message = event.message
                }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .imePadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scroll),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthTopScreen()
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xxl))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.md),
                horizontalArrangement = Arrangement.Start
            ){

            Text(
                "Email",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground)
            }

            AuthTextField(
                value = uiState.email,
                onValueChange = viewmodel::onEmailChange,
                placeholder = {
                    Text(
                        "email",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Mail,
                        contentDescription = null
                    )
                },
                isError = uiState.emailError != null,
                errorMsg = uiState.emailError,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.md),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    "Password",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            AuthTextField(
                value = uiState.password,
                onValueChange = viewmodel::onPasswordChange,
                placeholder = {
                    Text(
                        "Enter your password",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = null
                    )
                },
                visualTransformation = if (uiState.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = viewmodel::togglePasswordVisibility) {
                        Icon(
                            imageVector = Icons.Default.RemoveRedEye,
                            contentDescription = null,
                            tint = if (uiState.isPasswordVisible) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                        )
                    }

                },
                isError = uiState.passwordError != null,
                errorMsg = uiState.passwordError,
                textStyle = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.md)
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.xxl))

            CustomButton(
                text = "Sign up",
                onClick = { viewmodel.signIn() },
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                isLoading = signUpState == SignInState.Loading,
                modifier = Modifier
                    .padding(horizontal = MaterialTheme.spacing.md)
                    .fillMaxWidth()
                    .height(62.dp)
                    .padding(MaterialTheme.spacing.sm)

            )
            Spacer(modifier = Modifier.weight(1f))
            BottomCard(onSignUpClicked = viewmodel::onSignUpClicked )
        }

        CustomToast(
            message = message,
            type = toastType,
            visibility = showToast,
            onDismiss = { showToast = !showToast }
        )
    }
}


