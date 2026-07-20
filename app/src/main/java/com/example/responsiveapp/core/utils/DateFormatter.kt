package com.example.responsiveapp.core.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val DATE_KEY_FORMATTER =
    DateTimeFormatter.ofPattern("yyyy-MM-dd")

fun Long.toLocalDateKey(
    zoneId: ZoneId = ZoneId.systemDefault()
): String {

    return Instant
        .ofEpochMilli(this)
        .atZone(zoneId)
        .toLocalDate()
        .format(DATE_KEY_FORMATTER)
}