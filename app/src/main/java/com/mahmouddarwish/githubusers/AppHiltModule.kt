package com.mahmouddarwish.githubusers

import com.mahmouddarwish.githubusers.data.domain.use_cases.SearchUseCase
import com.mahmouddarwish.githubusers.data.domain.use_cases.UserDetailsUseCase
import com.mahmouddarwish.githubusers.data.network.api.Constants.API_BASE_URL
import com.mahmouddarwish.githubusers.data.network.api.GitHubService
import com.mahmouddarwish.githubusers.data.network.api.okHttpClient
import com.mahmouddarwish.githubusers.data.network.repos.GitHubUserRepo
import com.mahmouddarwish.githubusers.data.network.repos.UsersSearchRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * This module is used for providing instances of the desired classes throughout the app
 * */
@InstallIn(SingletonComponent::class)
@Module
object AppHiltModule {
    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideGithubApi(retrofit: Retrofit): GitHubService =
        retrofit.create(GitHubService::class.java)

    @Singleton
    @Provides
    fun provideUserDetailsRepo(service: GitHubService): UserDetailsUseCase =
        GitHubUserRepo(service)

    @Singleton
    @Provides
    fun provideSearchRepo(service: GitHubService): SearchUseCase =
        UsersSearchRepo(service)
}



