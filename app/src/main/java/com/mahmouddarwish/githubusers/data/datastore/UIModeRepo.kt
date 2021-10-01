package com.mahmouddarwish.githubusers.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.mahmouddarwish.githubusers.data.datastore.Constants.modeIntKey
import com.mahmouddarwish.githubusers.data.domain.use_cases.ChangeUIModeUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class UIModeRepo @Inject constructor(
    @ApplicationContext context: Context
) : ChangeUIModeUseCase {
    private val dataStore: DataStore<Preferences> = context.dataStore

    override suspend fun setUIMode(mode: ChangeUIModeUseCase.Mode) {
        dataStore.edit {
            it[modeIntKey] = mode.key
        }
    }

    override val isDayUIMode: Flow<Boolean> = dataStore.data.map {
        val modeKey = it[modeIntKey] ?: ChangeUIModeUseCase.Mode.Day.key
        modeKey == ChangeUIModeUseCase.Mode.Day.key
    }
}

private const val TAG = "UIModeRepo"

