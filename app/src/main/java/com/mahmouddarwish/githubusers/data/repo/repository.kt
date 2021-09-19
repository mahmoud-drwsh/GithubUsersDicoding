package com.mahmouddarwish.githubusers.data.repo

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.mahmouddarwish.githubusers.HomeViewModel
import com.mahmouddarwish.githubusers.R
import com.mahmouddarwish.githubusers.UNKNOWN_ERROR_MESSAGE
import com.mahmouddarwish.githubusers.model.GitHubUsers
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubUsersRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    val users = flow {
        emit(HomeViewModel.HomeUIStates.Loading)
        val jsonUsersData =
            context.resources.openRawResource(R.raw.github_users).readBytes()
                .decodeToString()
        try {
            val gson = Gson()
            val usersData = gson.fromJson(jsonUsersData, GitHubUsers::class.java)
            emit(HomeViewModel.HomeUIStates.Loaded(usersData.users.filter { it.name != null }))
        } catch (e: JsonSyntaxException) {
            emit(HomeViewModel.HomeUIStates.Error(e.message ?: UNKNOWN_ERROR_MESSAGE))
        } catch (e: Exception) {
            emit(HomeViewModel.HomeUIStates.Error(e.message ?: UNKNOWN_ERROR_MESSAGE))
        }
    }

}