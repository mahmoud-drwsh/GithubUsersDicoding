package com.mahmouddarwish.githubusers.screens.favorites

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.mahmouddarwish.githubusers.R
import com.mahmouddarwish.githubusers.data.datastore.UIModeRepo
import com.mahmouddarwish.githubusers.data.room.FavoritesRepo
import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repo: FavoritesRepo,
    private val uiRepo: UIModeRepo,
    @ApplicationContext context: Context,
) : ViewModel() {
    private val resources: Resources = context.resources

    val darkModeEnabled: Flow<Boolean> = uiRepo.isDarkUIMode

    val favoritesUIState: Flow<FavoritesUIStates> = flow {
        emit(FavoritesUIStates.Loading)
        try {
            val users = withContext(IO) { repo.getAll() }
            if (users.isNotEmpty())
                emit(FavoritesUIStates.Populated(users))
            else
                emit(FavoritesUIStates.Error(resources.getString(R.string.no_favorites_message)))
        } catch (e: Exception) {
            emit(
                FavoritesUIStates.Error(
                    e.message ?: resources.getString(R.string.unknown_error_message)
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