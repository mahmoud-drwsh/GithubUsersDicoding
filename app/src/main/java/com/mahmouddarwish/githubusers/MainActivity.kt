package com.mahmouddarwish.githubusers

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.mahmouddarwish.githubusers.data.repo.GitHubUsersRepository
import com.mahmouddarwish.githubusers.model.GitHubUsers
import com.mahmouddarwish.githubusers.model.UserData
import com.mahmouddarwish.githubusers.ui.CenteredContent
import com.mahmouddarwish.githubusers.ui.CenteredText
import com.mahmouddarwish.githubusers.ui.theme.GithubUsersTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "MainActivity"

@HiltViewModel
class HomeViewModel @Inject constructor(
    handle: SavedStateHandle,
    @ApplicationContext context: Context,
    usersRepository: GitHubUsersRepository
) : ViewModel() {

    val users: Flow<HomeUIStates> = usersRepository.users;

    sealed class HomeUIStates {
        object Loading : HomeUIStates()
        class Error(val message: String) : HomeUIStates()
        class Loaded(val data: List<UserData>) : HomeUIStates()
    }
}

@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.toString()
        setContent {
            val uiState by viewModel.users.collectAsState(initial = HomeViewModel.HomeUIStates.Loading)
            GithubUsersTheme {
                HomeScreen(uiState)
            }
        }
    }

    @Composable
    private fun HomeScreen(
        uiState: HomeViewModel.HomeUIStates,
    ) {
        when (uiState) {
            is HomeViewModel.HomeUIStates.Error -> {
                CenteredText(uiState.message)
            }
            is HomeViewModel.HomeUIStates.Loaded -> {
                LazyColumn(
                    verticalArrangement = spacedBy(4.dp),
                    contentPadding = PaddingValues(4.dp)
                ) {
                    items(uiState.data) {
                        UserRowItem(it)
                    }
                }
            }
            HomeViewModel.HomeUIStates.Loading -> {
                CenteredContent {
                    CircularProgressIndicator(modifier = Modifier.size(32.dp))
                    Text("Loading")
                }
            }
        }
    }

    @Composable
    fun UserRowItem(user: UserData) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = 8.dp
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .height(128.dp)
                    .padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.25f)
                        .fillMaxHeight()
                        .size(128.dp)
                ) {
                    CoilCustomImage(url = user.avatarUrl)
                }
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${user.login}", modifier = Modifier.fillMaxWidth())
                    Text(text = "${user.name}", modifier = Modifier.fillMaxWidth())
                    user.bio?.let {
                        Text(
                            text = it.trim(),
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Composable
fun CoilCustomImage(url: String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        val painter =
            rememberImagePainter(data = url) {
                size(OriginalSize)
            }

        if (painter.state !is ImagePainter.State.Success && painter.state is ImagePainter.State.Loading || painter.state is ImagePainter.State.Empty) {
            CircularProgressIndicator()
        } else {
            LaunchedEffect(key1 = url, block = {
                Log.e(TAG, "CoilCustomImage: $url")
            })

            Image(
                painter = painter,
                contentDescription = "",
                modifier = Modifier
                    .clip(CircleShape),
                // contentScale = ContentScale.Crop
            )
        }
    }
}
