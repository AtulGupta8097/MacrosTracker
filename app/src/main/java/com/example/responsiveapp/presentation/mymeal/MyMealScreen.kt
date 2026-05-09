package com.example.responsiveapp.presentation.mymeal

import com.example.responsiveapp.presentation.mymeal.component.MyMealPhoneLayout
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.responsiveapp.presentation.ui.theme.DeviceConfiguration
import com.example.responsiveapp.presentation.ui.theme.deviceConfiguration

@Composable
fun MyMealScreen(
    viewModel: MyMealViewModel = hiltViewModel(),
    onRootChanged: (Boolean) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val totalMacros = state.ingredient.totalMacros()

    val deviceConfiguration = MaterialTheme.deviceConfiguration

    when (deviceConfiguration) {
        DeviceConfiguration.MOBILE -> {
            MyMealPhoneLayout(
                state = state,
                totalMacros = totalMacros,
                onCreateMealClick = viewModel::onCreateMealClicked,
                onRootChanged = onRootChanged,
                onMealNameChange = viewModel::onMealNameChange,
                onBack = viewModel::onBack
            )
        }

        DeviceConfiguration.TABLET -> {
            MyMealPhoneLayout(
                state = state,
                totalMacros = totalMacros,
                onCreateMealClick = viewModel::onCreateMealClicked,
                onRootChanged = onRootChanged,
                onMealNameChange = viewModel::onMealNameChange,
                onBack = viewModel::onBack
            )
        }

        else -> {
            MyMealTabLayout()
        }
    }

}

@Composable
fun MyMealTabLayout() {
    // TODO: Tablet UI
}



