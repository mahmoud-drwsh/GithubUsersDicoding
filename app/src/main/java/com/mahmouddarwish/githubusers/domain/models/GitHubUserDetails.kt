package com.mahmouddarwish.githubusers.domain.models


import com.google.gson.annotations.SerializedName

data class GitHubUserDetails(
    @SerializedName("avatar_url")
    val avatarUrl: String = "",
    @SerializedName("bio")
    val bio: String? = null,
    @SerializedName("company")
    val company: String? = null,
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("email")
    val email: String? = null,
    @SerializedName("followers")
    val followers: Int = 0,
    @SerializedName("following")
    val following: Int = 0,
    @SerializedName("html_url")
    val htmlUrl: String = "",
    @SerializedName("location")
    val location: String? = null,
    @SerializedName("login")
    val login: String = "",
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("public_gists")
    val publicGists: Int = 0,
    @SerializedName("public_repos")
    val publicRepos: Int = 0,
    @SerializedName("url")
    val url: String = ""
) {
    fun toGitHubUser(): GitHubUser = GitHubUser(
        avatarUrl = avatarUrl,
        htmlUrl = htmlUrl,
        login = login,
        url = url
    )
}