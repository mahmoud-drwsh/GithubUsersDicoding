package com.mahmouddarwish.githubusers.screens.favorites

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.mahmouddarwish.githubusers.R
import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import com.mahmouddarwish.githubusers.domain.use_cases.ManageFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModel @Inject constructor(
    private val repo: ManageFavoritesUseCase,
    private val resources: Resources,
) : ViewModel() {

    val favoritesUIState: Flow<FavoritesUIStates> = channelFlow {
        send(FavoritesUIStates.Loading)

        try {
            repo.getAllFlow().collect { users ->
                if (users.isNotEmpty())
                    send(FavoritesUIStates.Populated(users))
                else
                    send(FavoritesUIStates.Error(resources.getString(R.string.no_favorites_message)))
            }
        } catch (e: Exception) {
            send(
                FavoritesUIStates.Error(
                    e.message
                        ?: resources.getString(R.string.unknown_error_message)
                )
            )
        }
    }

    sealed class FavoritesUIStates {
        class Error(val message: String) : FavoritesUIStates()
        object Loading : FavoritesUIStates()
        class Populated(val users: List<GitHubUser>) : FavoritesUIStates()
    }
}