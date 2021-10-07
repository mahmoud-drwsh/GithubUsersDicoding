package com.mahmouddarwish.githubusers.domain.use_cases

import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import com.mahmouddarwish.githubusers.domain.models.GitHubUserDetails

/**
 * This interface represents the features required in the details screen
 * */
interface UserDetailsUseCase {
    suspend fun getUserDetails(id: String): GitHubUserDetails

    suspend fun getFollowers(id: String): List<GitHubUser>

    suspend fun getFollowing(id: String): List<GitHubUser>
}