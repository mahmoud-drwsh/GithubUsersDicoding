package com.mahmouddarwish.githubusers.data.network.api

import com.mahmouddarwish.githubusers.BuildConfig

object Constants {
    const val API_BASE_URL = "https://api.github.com/"
    internal const val TOKEN: String = BuildConfig.API_KEY
}