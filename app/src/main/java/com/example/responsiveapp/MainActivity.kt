package com.example.responsiveapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.responsiveapp.core.navigation.Routes
import com.example.responsiveapp.presentation.main_screen.MainScreen
import com.example.responsiveapp.presentation.signin_screen.SignInScreen
import com.example.responsiveapp.presentation.signup_screen.SignUpScreen
import com.example.responsiveapp.presentation.splash_screen.SplashScreen
import com.example.responsiveapp.presentation.ui.theme.ResponsiveAppTheme
import com.example.responsiveapp.presentation.user_setup_screens.screens.UserSetupMainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ResponsiveAppTheme {

                val backStack = rememberNavBackStack(
                    Routes.SplashScreen
                )
                    NavDisplay(
                        entryDecorators = listOf(
                            rememberSaveableStateHolderNavEntryDecorator(),
                            rememberViewModelStoreNavEntryDecorator()
                        ),
                        backStack = backStack,
                        entryProvider = entryProvider {

                            entry<Routes.SplashScreen> {
                                SplashScreen(
                                    navigateToSignInScreen = {
                                        backStack.clear()
                                        backStack.add(Routes.SignInScreen)
                                    },
                                    navigateToMainScreen = {
                                        backStack.clear()
                                        backStack.add(Routes.MainScreen)
                                    },
                                    navigateToUserSetupScreen = {
                                        backStack.clear()
                                        backStack.add(Routes.UserSetupScreen)
                                    }
                                )
                            }

                            entry<Routes.SignInScreen> {
                                SignInScreen(
                                    navigateToSignUp = {
                                        backStack.clear()
                                        backStack.add(Routes.SignUpScreen)
                                    },
                                    navigateToMainScreen = {
                                        backStack.clear()
                                        backStack.add(Routes.MainScreen)
                                    }
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
                                MainScreen()
                            }
                        }
                    )
            }
        }
    }
}

