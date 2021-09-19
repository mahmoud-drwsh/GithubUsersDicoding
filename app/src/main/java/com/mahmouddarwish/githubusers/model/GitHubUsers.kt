package com.mahmouddarwish.githubusers.model


import com.google.gson.annotations.SerializedName

data class GitHubUsers(
    @SerializedName("userData")
    val users: List<UserData> = listOf()
)