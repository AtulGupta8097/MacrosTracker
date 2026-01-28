package com.example.responsiveapp.core.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay

@Composable
fun UserSetupNav() {

    val backStack = rememberNavBackStack(
        Routes.UserSetupScreen.GenderScreen
    )
    NavDisplay(
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        backStack = backStack,
        entryProvider = entryProvider {

            entry<Routes.UserSetupScreen.GenderScreen> {

            }

            entry<Routes.UserSetupScreen.AgeScreen> {

            }

        }
    )

}