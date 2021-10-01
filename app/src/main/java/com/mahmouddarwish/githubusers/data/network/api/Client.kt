package com.mahmouddarwish.githubusers.data.network.api

import okhttp3.OkHttpClient

/**
 * This custom client is used in order to pass an authentication interceptor to Retrofit
 * */
val okHttpClient: OkHttpClient =
    OkHttpClient
        .Builder()
        .addInterceptor(AuthenticationHeaderAddingInterceptor)
        .build()

