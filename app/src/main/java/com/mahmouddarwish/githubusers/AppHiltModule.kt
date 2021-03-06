package com.mahmouddarwish.githubusers

import android.content.Context
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.mahmouddarwish.githubusers.data.datastore.dataStore
import com.mahmouddarwish.githubusers.data.network.api.Constants.API_BASE_URL
import com.mahmouddarwish.githubusers.data.network.api.GitHubService
import com.mahmouddarwish.githubusers.data.network.api.okHttpClient
import com.mahmouddarwish.githubusers.data.repos.FavoritesRepo
import com.mahmouddarwish.githubusers.data.repos.GitHubUserRepo
import com.mahmouddarwish.githubusers.data.repos.UILightModeRepo
import com.mahmouddarwish.githubusers.data.repos.UsersSearchRepo
import com.mahmouddarwish.githubusers.data.room.FavoritesDao
import com.mahmouddarwish.githubusers.data.room.FavoritesRoomDatabase
import com.mahmouddarwish.githubusers.domain.use_cases.ChangeUILightModeUseCase
import com.mahmouddarwish.githubusers.domain.use_cases.ManageFavoritesUseCase
import com.mahmouddarwish.githubusers.domain.use_cases.SearchUseCase
import com.mahmouddarwish.githubusers.domain.use_cases.UserDetailsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Singleton
    @Provides
    fun provideFavoritesRepo(favoritesRepo: FavoritesRepo): ManageFavoritesUseCase = favoritesRepo

    @Singleton
    @Provides
    fun provideUIModeRepo(uiModeRepo: UILightModeRepo): ChangeUILightModeUseCase = uiModeRepo

    @Singleton
    @Provides
    fun provideRoomDb(@ApplicationContext context: Context): FavoritesRoomDatabase {
        return Room.databaseBuilder(context, FavoritesRoomDatabase::class.java, "main_db").build()
    }

    @Singleton
    @Provides
    fun provideFavoritesDao(favoritesRoomDatabase: FavoritesRoomDatabase): FavoritesDao {
        return favoritesRoomDatabase.FavoritesDao()
    }

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStore

    @Singleton
    @Provides
    fun provideAppResources(@ApplicationContext context: Context): Resources = context.resources
}


