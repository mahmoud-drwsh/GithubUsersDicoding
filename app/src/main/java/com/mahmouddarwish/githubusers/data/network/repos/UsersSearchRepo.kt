package com.mahmouddarwish.githubusers.data.network.repos

import com.mahmouddarwish.githubusers.data.domain.models.GitHubUser
import com.mahmouddarwish.githubusers.data.domain.use_cases.SearchUseCase
import com.mahmouddarwish.githubusers.data.network.api.GitHubService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersSearchRepo @Inject constructor(
    private val service: GitHubService
) : SearchUseCase {
    override suspend fun searchUsers(query: String): List<GitHubUser> =
        service.searchUsers(query).users
}