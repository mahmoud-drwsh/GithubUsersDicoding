package com.mahmouddarwish.githubusers.data.domain.use_cases

import kotlinx.coroutines.flow.Flow

interface ChangeUIModeUseCase {
    suspend fun setUIMode(mode: Mode)

    val isDayUIMode: Flow<Boolean>

    sealed class Mode(val key: Int) {
        object Night : Mode(0)
        object Day : Mode(1)

        companion object {
            fun keyToMode(key: Int): Mode {
                return when (key) {
                    0 -> Night
                    1 -> Day
                    // The default value
                    else -> Day
                }
            }
        }
    }
}