package com.mahmouddarwish.githubusers.data.repos

import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import com.mahmouddarwish.githubusers.domain.use_cases.SearchUseCase
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