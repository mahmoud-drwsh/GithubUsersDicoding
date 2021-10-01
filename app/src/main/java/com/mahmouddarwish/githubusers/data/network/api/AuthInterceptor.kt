package com.mahmouddarwish.githubusers.data.network.api

import com.mahmouddarwish.githubusers.data.network.api.Constants.TOKEN
import okhttp3.Interceptor

/**
 * This interceptor is used to add the authentication header for every request
 * */
internal val AuthenticationHeaderAddingInterceptor = Interceptor { chain ->
    val request = chain
        .request()
        .newBuilder()
        .addHeader("Authorization", """token $TOKEN""")
        .build()

    chain.proceed(request)
}