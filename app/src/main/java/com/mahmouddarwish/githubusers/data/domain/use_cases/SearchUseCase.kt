package com.mahmouddarwish.githubusers.data.domain.use_cases

import com.mahmouddarwish.githubusers.data.domain.models.GitHubUser

/**
 * This interface represents the feature of searching
 * */
interface SearchUseCase {
    suspend fun searchUsers(query: String): List<GitHubUser>
}