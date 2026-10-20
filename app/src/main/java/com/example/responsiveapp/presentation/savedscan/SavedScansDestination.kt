package com.example.responsiveapp.presentation.savedscan

import androidx.compose.runtime.Immutable

@Immutable
sealed interface SavedScansDestination {
    data object List : SavedScansDestination
    data class Detail(val foodId: String) : SavedScansDestination
}
