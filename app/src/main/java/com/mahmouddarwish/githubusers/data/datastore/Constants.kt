package com.mahmouddarwish.githubusers.data.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey

object Constants {
    internal val enableDarkModeKey: Preferences.Key<Boolean> = booleanPreferencesKey("ui_mode")
}