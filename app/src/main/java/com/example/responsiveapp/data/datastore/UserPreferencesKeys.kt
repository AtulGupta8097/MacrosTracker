package com.example.responsiveapp.data.datastore

import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object UserPreferencesKeys {
    val GENDER = stringPreferencesKey("gender")
    val AGE = intPreferencesKey("age")
    val WEIGHT = floatPreferencesKey("weight")
    val HEIGHT = floatPreferencesKey("height")
    val ACTIVITY = stringPreferencesKey("activity")
    val GOAL = stringPreferencesKey("goal")
}