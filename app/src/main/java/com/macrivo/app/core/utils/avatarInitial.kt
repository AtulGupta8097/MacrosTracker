package com.macrivo.app.core.utils

fun String.avatarInitial(): String =
    trim()
        .firstOrNull()
        ?.uppercaseChar()
        ?.toString()
        ?: "?"
