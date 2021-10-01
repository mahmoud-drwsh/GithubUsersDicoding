package com.mahmouddarwish.githubusers.data.domain.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GitHubUser(
    @SerializedName("avatar_url")
    val avatarUrl: String = "",
    @SerializedName("html_url")
    val htmlUrl: String = "",
    @SerializedName("login")
    val login: String = "",
    @SerializedName("url")
    val url: String = ""
) : Parcelable