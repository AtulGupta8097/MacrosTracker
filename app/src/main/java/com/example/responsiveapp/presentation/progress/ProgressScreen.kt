package com.example.responsiveapp.presentation.progress

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.responsiveapp.presentation.progress.componet.NutritionOverviewSection
import com.example.responsiveapp.presentation.progress.componet.ProgressHeader
import com.example.responsiveapp.presentation.progress.componet.RangeSelector
import com.example.responsiveapp.presentation.progress.componet.macrosdistribution.MacroDistributionSection
import com.example.responsiveapp.presentation.ui.theme.spacing

@Composable
fun ProgressScreen(
    modifier: Modifier = Modifier,
    viewModel: ProgressViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = MaterialTheme.spacing.md,
                end = MaterialTheme.spacing.md,
                bottom = MaterialTheme.spacing.xl,
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.md),
        ) {

            item {
                ProgressHeader(modifier = Modifier.fillMaxWidth())
            }

            item {
                RangeSelector(
                    selected = state.range,
                    onRangeSelected = viewModel::onRangeSelected,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                NutritionOverviewSection(
                    state = state.nutritionOverview,
                    range = state.range,
                    selectedMacro = state.selectedMacro,
                    onMacroSelected = viewModel::onMacroSelected,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item{
                MacroDistributionSection(
                    state = state.macroDistribution,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}