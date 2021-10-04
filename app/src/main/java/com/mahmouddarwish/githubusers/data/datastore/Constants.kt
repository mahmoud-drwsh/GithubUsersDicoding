package com.mahmouddarwish.githubusers.data.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

object Constants {
    val enableDarkModeKey: Preferences.Key<Boolean> = booleanPreferencesKey("ui_mode")
}