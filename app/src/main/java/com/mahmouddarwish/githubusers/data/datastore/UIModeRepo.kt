package com.mahmouddarwish.githubusers.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.mahmouddarwish.githubusers.CoroutinesScopesModule
import com.mahmouddarwish.githubusers.data.datastore.Constants.enableDarkModeKey
import com.mahmouddarwish.githubusers.data.domain.use_cases.ChangeUIModeUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UIModeRepo @Inject constructor(
    @ApplicationContext context: Context
) : ChangeUIModeUseCase {
    private val dataStore: DataStore<Preferences> = context.dataStore

    override val isDarkUIMode: Flow<Boolean> =
        dataStore.data.map { preferences -> preferences[enableDarkModeKey] ?: false }

    override suspend fun toggleUIMode() {
        dataStore.edit { preferences ->
            val previousEnableDarkModeValue = preferences[enableDarkModeKey]
            val newEnableDarkMode = previousEnableDarkModeValue?.not() ?: true
            preferences[enableDarkModeKey] = newEnableDarkMode
        }
    }

}