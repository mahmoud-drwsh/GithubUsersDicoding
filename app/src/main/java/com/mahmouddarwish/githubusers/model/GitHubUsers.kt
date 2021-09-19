package com.mahmouddarwish.githubusers.model


import com.google.gson.annotations.SerializedName

/**
 * This class is used for representing the parsed content of the json file that is located in the raw
 * assets file
 * */
data class GitHubUsers(
    @SerializedName("userData")
    val githubUsers: List<GithubUserData> = listOf()
)