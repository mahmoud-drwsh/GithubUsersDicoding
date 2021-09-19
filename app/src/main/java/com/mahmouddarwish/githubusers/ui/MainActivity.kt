package com.mahmouddarwish.githubusers.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil.annotation.ExperimentalCoilApi
import com.mahmouddarwish.githubusers.model.GithubUserData
import com.mahmouddarwish.githubusers.ui.theme.GithubUsersTheme
import com.mahmouddarwish.githubusers.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "MainActivity"

@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.toString()
        setContent {
            val uiState by viewModel.users.collectAsState(initial = HomeViewModel.HomeUIState.Loading)
            GithubUsersTheme {
                HomeScreen(uiState) { githubUser: GithubUserData ->
                    val intent = Intent(this, DetailsActivity::class.java)
                    intent.putExtra(DetailsActivity.USER_INTENT_KEY, githubUser)
                    startActivity(intent)
                }
            }
        }
    }

    @Composable
    private fun HomeScreen(
        uiState: HomeViewModel.HomeUIState,
        onItemClick: (GithubUserData) -> Unit,
    ) {
        Scaffold(
            topBar = {
                TopAppBar(title = {
                    Text(text = "GitHub Users")
                })
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues))
            when (uiState) {
                is HomeViewModel.HomeUIState.Error -> {
                    CenteredText(uiState.message)
                }
                is HomeViewModel.HomeUIState.Loaded -> {
                    LazyColumn(
                        verticalArrangement = spacedBy(8.dp),
                        contentPadding = PaddingValues(8.dp)
                    ) {
                        item {
                            Text(
                                text = "Some Github Users with Mahmoud in Their Names",
                                modifier = Modifier.padding(8.dp),
                                style = MaterialTheme.typography.h6
                            )
                        }
                        items(uiState.data) {
                            UserRowItem(it, onItemClick)
                        }

                        item {
                            Text(
                                text = "Displayed users count: ${uiState.data.size}",
                                Modifier.padding(8.dp)
                            )
                        }
                    }
                }
                HomeViewModel.HomeUIState.Loading -> {
                    CenteredContent {
                        CircularProgressIndicator(modifier = Modifier.size(32.dp))
                        Text("Loading")
                    }
                }
            }

        }

    }

    @Composable
    fun UserRowItem(githubUser: GithubUserData, onItemClick: (GithubUserData) -> Unit) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(128.dp)
                .clickable {
                    onItemClick(githubUser)
                },
            elevation = 8.dp
        ) {
            ConstraintLayout(Modifier.padding(8.dp)) {
                val (image, name, bio, username) = createRefs()

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(128.dp)
                        .clip(CircleShape.copy(CornerSize(8.dp)))
                        .padding(end = 4.dp)
                        .constrainAs(image) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    CustomCoilImage(githubUser.avatarUrl)
                }

                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("ID: ")
                    }
                    append(githubUser.login)
                }, modifier = Modifier
                    .constrainAs(username) {
                        start.linkTo(name.start)
                        top.linkTo(name.bottom)
                    }
                    .fillMaxWidth(),
                    style = MaterialTheme.typography.caption)

                Text(text = "${githubUser.name}", modifier = Modifier
                    .constrainAs(name) {
                        start.linkTo(image.end)
                        top.linkTo(parent.top)
                    }
                    .fillMaxWidth())

                githubUser.bio?.let {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("BIO: ")
                            }
                            append(it.trim())
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(bio) {
                                start.linkTo(name.start)
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                                width = Dimension.fillToConstraints
                            }
                            .background(Color.LightGray.copy(alpha = 0.1f)),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

}

