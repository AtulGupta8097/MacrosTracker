package com.macrivo.app

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.macrivo.app.core.navigation.Routes
import com.macrivo.app.domain.model.ThemeMode
import com.macrivo.app.presentation.forgetpassword.ForgotPasswordScreen
import com.macrivo.app.presentation.mainscreen.MainScreen
import com.macrivo.app.presentation.signin.SignInScreen
import com.macrivo.app.presentation.signup.SignUpScreen
import com.macrivo.app.presentation.splash.SplashViewModel
import com.macrivo.app.presentation.splash.setBrandExitAnimation
import com.macrivo.app.presentation.ui.theme.ResponsiveAppTheme
import com.macrivo.app.presentation.usersetup.screens.UserSetupMainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class  MainActivity : ComponentActivity() {

    private val splashViewModel: SplashViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {

        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition { splashViewModel.startDestination.value == null }
        splashScreen.setBrandExitAnimation()
        enableEdgeToEdge()


        setContent {
            val themeViewModel: AppThemeViewModel = hiltViewModel()
            val themeMode by themeViewModel.themeMode.collectAsStateWithLifecycle()

            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                else -> {
                    isSystemInDarkTheme()
                }
            }
            ResponsiveAppTheme(darkTheme = darkTheme) {

                // CHANGED: nothing is drawn until the destination is known (the system splash covers it)
                val startDestination by splashViewModel.startDestination.collectAsStateWithLifecycle()
                val start = startDestination ?: return@ResponsiveAppTheme

                val backStack = rememberNavBackStack(start)
                NavDisplay(
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator()
                    ),
                    backStack = backStack,
                    entryProvider = entryProvider {

                        entry<Routes.SignInScreen> {
                            SignInScreen(
                                navigateToSignUp = {
                                    backStack.clear()
                                    backStack.add(Routes.SignUpScreen)
                                },
                                navigateToForgotPassword = { email ->
                                    backStack.add(Routes.ForgotPasswordScreen(email))
                                },
                                navigateToMainScreen = {
                                    backStack.clear()
                                    backStack.add(Routes.MainScreen)
                                },
                                navigateToUserSetup = {
                                    backStack.clear()
                                    backStack.add(Routes.UserSetupScreen)
                                }
                            )

                        }

                        entry<Routes.ForgotPasswordScreen> { key ->
                            ForgotPasswordScreen(
                                initialEmail = key.email,
                                onBackToSignIn = { backStack.removeLastOrNull() }
                            )
                        }

                        entry<Routes.SignUpScreen> {
                            SignUpScreen(
                                navigateToSignIn = {
                                    backStack.clear()
                                    backStack.add(Routes.SignInScreen)
                                },
                                navigateToHome = {
                                    backStack.clear()
                                    backStack.add(Routes.MainScreen )
                                },
                                navigateToUserSetup = {
                                    backStack.clear()
                                    backStack.add(Routes.UserSetupScreen)
                                }
                            )
                        }


                        entry<Routes.UserSetupScreen> {
                            UserSetupMainScreen(
                                navigateToMainScreen = {
                                    backStack.clear()
                                    backStack.add(Routes.MainScreen)
                                }

                            )
                        }

                        entry<Routes.MainScreen> {
                            MainScreen(
                                onSignedOut = {
                                    backStack.clear()
                                    backStack.add(Routes.SignInScreen)
                                },
                                onAccountDeleted = {
                                    backStack.clear()
                                    backStack.add(Routes.SignInScreen)
                                },
                            )
                        }
                    }
                )
            }
        }
    }
}

