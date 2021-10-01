package com.mahmouddarwish.githubusers.data.network.repos

import com.mahmouddarwish.githubusers.data.domain.models.GitHubUser
import com.mahmouddarwish.githubusers.data.domain.models.GitHubUserDetails
import com.mahmouddarwish.githubusers.data.domain.use_cases.UserDetailsUseCase
import com.mahmouddarwish.githubusers.data.network.api.GitHubService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubUserRepo @Inject constructor(
    private val service: GitHubService
) : UserDetailsUseCase {
    override suspend fun getUserDetails(id: String): GitHubUserDetails {
        return service.getUser(id)
    }

    override suspend fun getFollowers(id: String): List<GitHubUser> {
        return service.getFollowers(id)
    }

    override suspend fun getFollowing(id: String): List<GitHubUser> {
        return service.getFollowing(id)
    }
}