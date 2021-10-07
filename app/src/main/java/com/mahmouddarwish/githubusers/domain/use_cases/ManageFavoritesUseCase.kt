package com.mahmouddarwish.githubusers.domain.use_cases

import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import kotlinx.coroutines.flow.Flow

interface ManageFavoritesUseCase {
    suspend fun addFavorite(favorite: GitHubUser)
    suspend fun removeFavorite(favorite: GitHubUser)
    fun getAll(): List<GitHubUser>
}