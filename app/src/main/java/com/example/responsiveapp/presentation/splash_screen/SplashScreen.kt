package com.example.responsiveapp.presentation.splash_screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.responsiveapp.R
import com.example.responsiveapp.core.navigation.Routes
import com.example.responsiveapp.presentation.ui.theme.spacing
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = hiltViewModel(),
    navigateToSignInScreen: () -> Unit,
    navigateToMainScreen: () -> Unit,
    navigateToUserSetupScreen: () -> Unit
) {
    val scale = remember { Animatable(0f) }
    val circleScale = remember { Animatable(1f) }
    var flag by remember { mutableStateOf(false) }
    var isText by remember { mutableStateOf(false) }
    val destination by splashViewModel.startDestination.collectAsState(
        initial = Routes.SignUpScreen
    )
    LaunchedEffect(Unit) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        )
        flag = true
        circleScale.animateTo(
            targetValue = 5f,
            animationSpec = tween(
                durationMillis = 1000,
                easing = {it}
            )
        )
        delay(500)
        isText = true
        delay(1000)
        when(destination) {
            Routes.MainScreen -> navigateToMainScreen.invoke()
            Routes.SignInScreen -> navigateToSignInScreen.invoke()
            Routes.UserSetupScreen -> navigateToUserSetupScreen.invoke()
            else -> Unit
        }
    }
    Box(modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center)
    {
        Box(modifier = Modifier
            .size(200.dp)
            .scale(circleScale.value)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = if (flag) listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.background
                    ) else listOf
                        (
                        Color.Transparent,
                        Color.Transparent
                    )
                )
            )
        )
        Column(verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {

            Image(
                modifier = Modifier.size(170.dp)
                    .scale(scale.value),
                painter = painterResource(id = R.drawable.logo),
                contentScale = ContentScale.FillBounds,
                contentDescription = null
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.md))

            AnimatedVisibility(isText) {

                Text(text = "TrackMacros", style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondary))

            }
        }
    }
}