package com.mahmouddarwish.githubusers.data.domain.use_cases

import kotlinx.coroutines.flow.Flow

interface ChangeUIModeUseCase {
    suspend fun toggleUIMode()

    val isDarkUIMode: Flow<Boolean>
}