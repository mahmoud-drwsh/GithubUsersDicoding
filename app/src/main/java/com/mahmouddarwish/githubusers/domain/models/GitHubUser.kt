package com.mahmouddarwish.githubusers.domain.models


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class GitHubUser(
    @SerializedName("avatar_url")
    val avatarUrl: String = "",
    @SerializedName("html_url")
    val htmlUrl: String = "",
    @SerializedName("login")
    @PrimaryKey
    val login: String = "",
    @SerializedName("url")
    val url: String = ""
) : Parcelable
