package com.mahmouddarwish.githubusers.data.network.api

import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import com.mahmouddarwish.githubusers.domain.models.GitHubUserDetails
import com.mahmouddarwish.githubusers.domain.models.GithubUsersSearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GitHubService {
    @GET("search/users")
    suspend fun searchUsers(@Query("q") name: String = ""): GithubUsersSearchResponse

    @GET("users/{user}")
    suspend fun getUser(@Path("user") userId: String): GitHubUserDetails

    @GET("/users/{user}/following")
    suspend fun getFollowing(@Path("user") userId: String): List<GitHubUser>

    @GET("/users/{user}/followers")
    suspend fun getFollowers(@Path("user") userId: String): List<GitHubUser>
}

