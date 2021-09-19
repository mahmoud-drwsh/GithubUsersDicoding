package com.mahmouddarwish.githubusers.viewmodel

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.mahmouddarwish.githubusers.Resource
import com.mahmouddarwish.githubusers.data.repo.GitHubUsersRepository
import com.mahmouddarwish.githubusers.model.GithubUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    handle: SavedStateHandle,
    @ApplicationContext context: Context,
    usersRepository: GitHubUsersRepository
) : ViewModel() {

    val users: Flow<HomeUIState> = usersRepository.users.map {
        when (it) {
            is Resource.Error -> HomeUIState.Error(it.message)
            Resource.Loading -> HomeUIState.Loading
            is Resource.Loaded -> HomeUIState.Loaded(it.users)
        }
    }

    sealed class HomeUIState {
        object Loading : HomeUIState()
        class Error(val message: String) : HomeUIState()
        class Loaded(val data: List<GithubUserData>) : HomeUIState()
    }
}