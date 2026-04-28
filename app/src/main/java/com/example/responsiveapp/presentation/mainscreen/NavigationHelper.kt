package com.example.responsiveapp.presentation.mainscreen

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.navigateSingleTop(route: NavKey) {
    if (last() == route) return

    val existingIndex = indexOfLast { it == route }
    if (existingIndex != -1) {
        repeat(size - existingIndex - 1) {
            removeAt(lastIndex)
        }
        removeAt(lastIndex)
    }
    add(route)
}
