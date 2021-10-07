package com.mahmouddarwish.githubusers.screens.favorites

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mahmouddarwish.githubusers.navigateUp
import com.mahmouddarwish.githubusers.screens.details.DetailsActivity
import com.mahmouddarwish.githubusers.ui.components.CenteredLoadingMessageWithIndicator
import com.mahmouddarwish.githubusers.ui.components.CenteredText
import com.mahmouddarwish.githubusers.ui.components.GithubUsersList
import com.mahmouddarwish.githubusers.ui.theme.GithubUsersTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesActivity : ComponentActivity() {
    val viewModel: FavoritesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val darkThemeEnabled by viewModel.darkModeEnabled.collectAsState(initial = false)

            val favoritesUIState: FavoritesViewModel.FavoritesUIStates by viewModel.favoritesUIState
                .collectAsState(initial = FavoritesViewModel.FavoritesUIStates.Loading)

            GithubUsersTheme(darkTheme = darkThemeEnabled) {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text(text = "Favorites") },
                            navigationIcon = {
                                IconButton(onClick = { navigateUp() }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "")
                                }
                            })
                    }
                ) {
                    when (favoritesUIState) {
                        FavoritesViewModel.FavoritesUIStates.Loading -> CenteredLoadingMessageWithIndicator()
                        is FavoritesViewModel.FavoritesUIStates.Error -> {
                            val error = favoritesUIState as FavoritesViewModel.FavoritesUIStates.Error
                            CenteredText(text = error.message)
                        }
                        is FavoritesViewModel.FavoritesUIStates.Populated -> {
                            val populated =
                                favoritesUIState as FavoritesViewModel.FavoritesUIStates.Populated
                            FavoritesScreenContent(populated)
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun FavoritesScreenContent(populated: FavoritesViewModel.FavoritesUIStates.Populated) {
        GithubUsersList(users = populated.users) { gitHubUser ->
            val intent =
                DetailsActivity.createIntentWithGithubUserData(this, gitHubUser)
            startActivity(intent)
        }
    }


    companion object {
        fun Context.navigateToFavoritesActivity() {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
        }
    }
}

