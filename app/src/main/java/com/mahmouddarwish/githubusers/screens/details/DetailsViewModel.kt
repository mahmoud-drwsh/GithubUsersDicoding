package com.mahmouddarwish.githubusers.screens.details

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.mahmouddarwish.githubusers.CoroutinesScopesModule
import com.mahmouddarwish.githubusers.R
import com.mahmouddarwish.githubusers.Resource
import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import com.mahmouddarwish.githubusers.domain.models.GitHubUserDetails
import com.mahmouddarwish.githubusers.domain.use_cases.ManageFavoritesUseCase
import com.mahmouddarwish.githubusers.domain.use_cases.UserDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModel @Inject constructor(
    @CoroutinesScopesModule.ApplicationScope private val coroutineScope: CoroutineScope,
    private val userDetailsUseCase: UserDetailsUseCase,
    private val favoritesRepo: ManageFavoritesUseCase,
    private val resources: Resources,
) : ViewModel() {

    private val userDetailsFlow: MutableStateFlow<Resource<GitHubUserDetails>> =
        MutableStateFlow(Resource.Loading)

    val isUserAFavorite: Flow<Boolean> = favoritesRepo.getAllFlow()
        .combine(userDetailsFlow) { list: List<GitHubUser>, githubUserResource: Resource<GitHubUserDetails> ->
            when (githubUserResource) {
                is Resource.Success -> {
                    val userDetails = githubUserResource.data
                    list.contains(userDetails.toGitHubUser())
                }
                else -> false
            }
        }

    fun setUser(user: GitHubUser?) = coroutineScope.launch(IO) {
        userDetailsFlow.value =
            if (user != null) Resource.Success(getDetails(user))
            else Resource.Error(resources.getString(R.string.unknown_error_message))
    }

    fun toggleFavoriteStatus(githubUserDetails: GitHubUserDetails, isFavorite: Boolean) =
        if (isFavorite) {
            favoritesRepo.removeFavorite(githubUserDetails.toGitHubUser())
        } else {
            favoritesRepo.addFavorite(githubUserDetails.toGitHubUser())
        }

    private suspend fun getDetails(gitHubUser: GitHubUser): GitHubUserDetails =
        userDetailsUseCase.getUserDetails(gitHubUser.login)

    /**
     * This will try to get the details of user passed and then emit the Success UI state
     * else it will emit Error UI state in case an exception is thrown
     * */
    val detailsUIStateFlow = channelFlow {
        send(DetailsUIState.Loading)
        userDetailsFlow.collect { userResource ->
            when (userResource) {
                is Resource.Success -> send(DetailsUIState.Success(userResource.data))
                Resource.Loading -> send(DetailsUIState.Loading)
                is Resource.Error -> send(DetailsUIState.Error(userResource.message))
            }
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