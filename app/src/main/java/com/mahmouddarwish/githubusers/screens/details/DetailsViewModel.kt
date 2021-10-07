package com.mahmouddarwish.githubusers.screens.details

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.mahmouddarwish.githubusers.CoroutinesScopesModule
import com.mahmouddarwish.githubusers.R
import com.mahmouddarwish.githubusers.data.datastore.UIModeRepo
import com.mahmouddarwish.githubusers.data.room.FavoritesRepo
import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import com.mahmouddarwish.githubusers.domain.models.GitHubUserDetails
import com.mahmouddarwish.githubusers.domain.use_cases.UserDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    @ApplicationContext context: Context,
    @CoroutinesScopesModule.ApplicationScope private val coroutineScope: CoroutineScope,
    private val userDetailsUseCase: UserDetailsUseCase,
    private val uiModeRepo: UIModeRepo,
    private val favoritesRepo: FavoritesRepo
) : ViewModel() {
    private val resources: Resources = context.resources

    val isDarkModeEnabled: Flow<Boolean> = uiModeRepo.isDarkUIMode

    fun addToFavorites(user: GitHubUser) = coroutineScope.launch {
        favoritesRepo.addFavorite(user)
    }

    private suspend fun getDetails(gitHubUser: GitHubUser): GitHubUserDetails {
        return userDetailsUseCase.getUserDetails(gitHubUser.login)
    }

    /**
     * This will try to get the details of user passed and then emit the Success UI state
     * else it will emit Error UI state in case an exception is thrown
     * */
    fun createDetailsUIStateFlow(
        githubUserDetails: GitHubUser?
    ) = flow {
        emit(DetailsUIState.Loading)
        try {
            // For making sure the GitHub user object is not null. This state should never be
            if (githubUserDetails == null)
                throw IllegalStateException(resources.getString(R.string.unknown_error_message))

            val details = getDetails(githubUserDetails)
            emit(DetailsUIState.Success(details))
        } catch (e: Exception) {
            emit(
                DetailsUIState.Error(
                    e.message ?: resources.getString(R.string.unknown_error_message)
                )
            )
        }
    }

    /**
     * Will try to get the list of the people following the user. In case an error occurs, or the
     * list is empty, an appropriate error message will be emitted
     * */
    fun getFollowers(id: String): Flow<PeopleUIState> = flow {
        emit(PeopleUIState.Loading)
        try {
            val followers = userDetailsUseCase.getFollowers(id)
            if (followers.isNotEmpty())
                emit(PeopleUIState.Success(followers))
            else
                emit(PeopleUIState.Error(resources.getString(R.string.user_has_none_message)))
        } catch (e: Exception) {
            emit(
                PeopleUIState.Error(
                    e.message ?: resources.getString(R.string.unknown_error_message)
                )
            )
        }
    }

    /**
     * Will try to get the list of the users being followed by the user. In case an error occurs,
     * or the list is empty, an appropriate error message will be emitted
     * */
    fun getFollowing(id: String): Flow<PeopleUIState> = flow {
        emit(PeopleUIState.Loading)
        try {
            val following = userDetailsUseCase.getFollowing(id)
            if (following.isEmpty()) {
                emit(PeopleUIState.Error(resources.getString(R.string.user_has_none_message)))
            } else {
                emit(PeopleUIState.Success(following))
            }
        } catch (e: Exception) {
            emit(
                PeopleUIState.Error(
                    e.message ?: resources.getString(R.string.unknown_error_message)
                )
            )
        }
    }

    /**
     * For representing the different states the following and followers composable can be in
     * */
    sealed class PeopleUIState {
        object Loading : PeopleUIState()
        class Error(val message: String) : PeopleUIState()
        class Success(val users: List<GitHubUser>) : PeopleUIState()
    }


    /**
     * For representing the different states of the details screen UI.
     * */
    sealed class DetailsUIState {
        object Loading : DetailsUIState()
        class Error(val message: String) : DetailsUIState()
        class Success(val gitHubUserDetails: GitHubUserDetails) : DetailsUIState()
    }
}