package com.mahmouddarwish.githubusers.screens.home


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.ModeNight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import coil.annotation.ExperimentalCoilApi
import com.mahmouddarwish.githubusers.R
import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import com.mahmouddarwish.githubusers.screens.details.DetailsActivity
import com.mahmouddarwish.githubusers.screens.favorites.FavoritesActivity.Companion.navigateToFavoritesActivity
import com.mahmouddarwish.githubusers.screens.home.HomeViewModel.HomeUIState
import com.mahmouddarwish.githubusers.ui.components.CenteredLoadingMessageWithIndicator
import com.mahmouddarwish.githubusers.ui.components.CenteredText
import com.mahmouddarwish.githubusers.ui.components.GithubUsersList
import com.mahmouddarwish.githubusers.ui.theme.GitHubUsersCustomTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * In this screen, a list of users and their names, ID's, and short bios are displayed.
 * Alongside that, at the top of the list a header is displayed and at the bottom of the list
 * the number of users displayed is shown.
 *
 * The Compose ConstraintsLayout is used here as per the submission requirements.
 * */
@ExperimentalCoilApi
@OptIn(ExperimentalComposeUiApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var myGitHubUsersCustomTheme: GitHubUsersCustomTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This invocation is necessary in order to display the splash screen
        installSplashScreen()


        setContent {
            val uiState by viewModel.homeUIStateFlow.collectAsState(initial = HomeUIState.Loading)

            val darkUIModeEnabled by myGitHubUsersCustomTheme
                .isDarkUIMode
                .collectAsState(initial = false)

            myGitHubUsersCustomTheme.GithubUsersTheme {

                HomeScreen(
                    uiState = uiState,
                    darkUIModeEnabled = darkUIModeEnabled
                ) { githubUserData ->
                    navigateToDetailsActivity(githubUserData)
                }
            }
        }
    }


    @Composable
    private fun HomeScreen(
        modifier: Modifier = Modifier,
        uiState: HomeUIState,
        darkUIModeEnabled: Boolean,
        onUserRowItemClick: (GitHubUser) -> Unit,
    ) {
        Scaffold(modifier = modifier,
            topBar = {
                // The action bar which is used just to display a header for the screen
                TopAppBar(
                    title = {
                        Text(text = stringResource(R.string.home_title))
                    },
                    actions = {
                        IconButton(onClick = {
                            lifecycleScope.launch { viewModel.toggleUIMode() }
                        }) {
                            val icon =
                                if (darkUIModeEnabled) Icons.Default.LightMode
                                else Icons.Default.ModeNight

                            Icon(
                                icon,
                                contentDescription = stringResource(R.string.ui_light_mode_toggling_icon)
                            )
                        }

                        IconButton(onClick = {
                            navigateToFavoritesActivity()
                        }) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = stringResource(R.string.favorite_screens_icon)
                            )
                        }
                    })
            }
        ) { paddingValues ->
            // This box is used to make sure the padding values passed by the Scaffold composable
            // are applied
            Column(
                modifier = Modifier
                    .padding(paddingValues),
            ) {
                UsersSearchField()

                // Displaying the corresponding content to the UI state
                when (uiState) {
                    is HomeUIState.Error -> {
                        CenteredText(text = uiState.message)
                    }
                    is HomeUIState.Success -> {
                        GithubUsersList(
                            users = uiState.data,
                            onUserRowItemClick = onUserRowItemClick
                        )
                    }
                    HomeUIState.Loading -> {
                        CenteredLoadingMessageWithIndicator(Modifier)
                    }
                }
            }
        }
    }

    @Composable
    private fun UsersSearchField() {
        Surface(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp)
        ) {
            val queryString by viewModel.searchQuery.collectAsState("")
            val keyboardController = LocalSoftwareKeyboardController.current

            TextField(
                value = queryString,
                onValueChange = { newValue -> viewModel.setQuery(newValue) },
                modifier = Modifier
                    .clip(CircleShape)
                    .border(BorderStroke(1.dp, Color.LightGray), CircleShape),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_field_icon))
                },
                trailingIcon = {
                    if (queryString.isNotEmpty())
                        IconButton(onClick = { viewModel.setQuery(("")) }) {
                            Icon(
                                Icons.Outlined.Clear,
                                contentDescription = stringResource(R.string.clear_search_query_icon)
                            )
                        }
                },
                label = {
                    Text(
                        text = stringResource(R.string.search),
                        color = MaterialTheme.colors.onSurface
                    )
                },
                placeholder = { Text(text = stringResource(R.string.name)) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    keyboardController?.hide()
                })
            )
        }
    }

    companion object {
        internal fun MainActivity.navigateToDetailsActivity(githubUserDetails: GitHubUser) {
            val intent = DetailsActivity.createIntentWithGithubUserData(this, githubUserDetails)
            startActivity(intent)
        }
    }
}


