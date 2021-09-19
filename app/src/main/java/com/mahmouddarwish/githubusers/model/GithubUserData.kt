package com.mahmouddarwish.githubusers.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GithubUserData(
    @SerializedName("avatar_url")
    val avatarUrl: String = "",
    @SerializedName("bio")
    val bio: String? = null,
    @SerializedName("blog")
    val blog: String = "",
    @SerializedName("company")
    val company: String? = null,
    @SerializedName("created_at")
    val createdAt: String = "",
    @SerializedName("followers")
    val followers: Int = 0,
    @SerializedName("following")
    val following: Int = 0,
    @SerializedName("hireable")
    val hireable: Boolean? = null,
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
    @SerializedName("type")
    val type: String = "",
    @SerializedName("updated_at")
    val updatedAt: String = ""
) : Parcelable