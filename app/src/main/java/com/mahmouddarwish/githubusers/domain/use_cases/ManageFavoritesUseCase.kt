package com.mahmouddarwish.githubusers.domain.use_cases

import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import kotlinx.coroutines.flow.Flow

interface ManageFavoritesUseCase {
    fun addFavorite(favorite: GitHubUser)
    fun removeFavorite(favorite: GitHubUser)
    fun getAllFlow(): Flow<List<GitHubUser>>
}