package com.mahmouddarwish.githubusers.data.domain.models


import com.google.gson.annotations.SerializedName

data class GithubUsersSearchResponse(
    @SerializedName("items")
    val users: List<GitHubUser> = listOf()
)