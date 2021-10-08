package com.mahmouddarwish.githubusers.domain.use_cases

import kotlinx.coroutines.flow.Flow

interface ChangeUILightModeUseCase {
    suspend fun toggleUIMode()

    val isDarkUIMode: Flow<Boolean>
}