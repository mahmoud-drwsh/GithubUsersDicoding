package com.mahmouddarwish.githubusers.data.repo

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.mahmouddarwish.githubusers.Constants.UNKNOWN_ERROR_MESSAGE
import com.mahmouddarwish.githubusers.R
import com.mahmouddarwish.githubusers.Resource
import com.mahmouddarwish.githubusers.model.GitHubUsers
import com.mahmouddarwish.githubusers.model.GithubUserData
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubUsersRepository @Inject constructor(
    @ApplicationContext context: Context
) {
    val users: Flow<Resource<List<GithubUserData>>> = flow {
        emit(Resource.Loading)
        val jsonUsersData =
            context.resources.openRawResource(R.raw.github_users).readBytes()
                .decodeToString()
        try {
            val gson = Gson()
            val usersData = gson.fromJson(jsonUsersData, GitHubUsers::class.java)
            emit(Resource.Loaded(usersData.githubUsers.filter { it.name != null }))
        } catch (e: JsonSyntaxException) {
            emit(Resource.Error(e.message ?: UNKNOWN_ERROR_MESSAGE))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: UNKNOWN_ERROR_MESSAGE))
        }
    }

}