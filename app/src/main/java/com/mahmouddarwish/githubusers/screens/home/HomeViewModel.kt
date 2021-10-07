package com.mahmouddarwish.githubusers.screens.home

import android.content.Context
import android.content.res.Resources
import androidx.lifecycle.ViewModel
import com.mahmouddarwish.githubusers.R
import com.mahmouddarwish.githubusers.data.datastore.UIModeRepo
import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import com.mahmouddarwish.githubusers.domain.use_cases.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val searchRepo: SearchUseCase,
    private val uiModeRepo: UIModeRepo,
) : ViewModel() {
    private val resources: Resources = context.resources

    private val _searchQuery: MutableStateFlow<String> = MutableStateFlow("0")
    val searchQuery: StateFlow<String>
        get() = _searchQuery

    val dayUIModeEnabledFlow: Flow<Boolean> = uiModeRepo.isDarkUIMode

    suspend fun toggleUIMode() = uiModeRepo.toggleUIMode()

    /**
     * This flow will emit new search results as soon as searchQuery changes with a delay in order to
     * prevent making too many requests and to prevent making requests needlessly
     * */
    @OptIn(FlowPreview::class)
    val homeUIStateFlow: Flow<HomeUIState> = flow {
        searchQuery
            // adding a delay to avoid making requests needlessly
            .debounce(200L)
            .collect { query ->
                if (query.isEmpty()) {
                    emit(HomeUIState.Error(resources.getString(R.string.empty_query_string_message)))
                } else {
                    emit(HomeUIState.Loading)
                    try {
                        val searchResult = searchRepo.searchUsers(query)
                        if (searchResult.isEmpty()) {
                            emit(HomeUIState.Error(resources.getString(R.string.no_results_message)))
                        } else {
                            emit(HomeUIState.Success(searchResult))
                        }
                    } catch (e: HttpException) {
                        when (e.code()) {
                            422 -> emit(HomeUIState.Error(resources.getString(R.string.no_results_message)))
                            else -> emit(HomeUIState.Error(resources.getString(R.string.unexpected_network_error)))
                        }
                    } catch (e: Exception) {
                        /**
                         * This is a very bad practice since the message might reveal sensitive info, but
                         * since security isn't a concern, I will leave it like this.
                         * */
                        emit(
                            HomeUIState.Error(
                                e.message ?: resources.getString(R.string.unknown_error_message)
                            )
                        )
                    }
                }
            }

    }

    fun setQuery(query: String) {
        _searchQuery.value = query
    }

    sealed class HomeUIState {
        object Loading : HomeUIState()
        class Error(val message: String) : HomeUIState()
        class Success(val data: List<GitHubUser>) : HomeUIState()
    }
}


