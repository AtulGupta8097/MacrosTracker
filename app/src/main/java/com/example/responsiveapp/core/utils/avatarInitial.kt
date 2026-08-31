package com.example.responsiveapp.core.utils

fun String.avatarInitial(): String =
    trim()
        .firstOrNull()
        ?.uppercaseChar()
        ?.toString()
        ?: "?"
