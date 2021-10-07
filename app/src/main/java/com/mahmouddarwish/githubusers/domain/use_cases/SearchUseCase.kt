package com.mahmouddarwish.githubusers.domain.use_cases

import com.mahmouddarwish.githubusers.domain.models.GitHubUser

/**
 * This interface represents the feature of searching
 * */
interface SearchUseCase {
    suspend fun searchUsers(query: String): List<GitHubUser>
}