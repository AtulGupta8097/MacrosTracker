package com.example.responsiveapp.core.utils

fun formatMacroValue(value: Float): String {
    return if (value % 1f == 0f) {
        value.toInt().toString()
    } else {
        ((value * 10).toInt() / 10f).toString()
    }
}