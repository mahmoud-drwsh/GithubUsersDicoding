package com.mahmouddarwish.githubusers.data.datastore

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.intPreferencesKey

object Constants {
    val modeIntKey: Preferences.Key<Int> = intPreferencesKey("ui_mode")
}