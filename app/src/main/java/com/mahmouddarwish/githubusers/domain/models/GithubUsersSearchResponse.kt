package com.mahmouddarwish.githubusers.domain.models


import com.google.gson.annotations.SerializedName

data class GithubUsersSearchResponse(
    @SerializedName("items")
    val users: List<GitHubUser> = listOf()
)