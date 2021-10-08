package com.mahmouddarwish.githubusers.screens.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.People
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.mahmouddarwish.githubusers.*
import com.mahmouddarwish.githubusers.R
import com.mahmouddarwish.githubusers.domain.models.GitHubUser
import com.mahmouddarwish.githubusers.domain.models.GitHubUserDetails
import com.mahmouddarwish.githubusers.ui.components.CoilImage
import com.mahmouddarwish.githubusers.ui.components.CenteredLoadingMessageWithIndicator
import com.mahmouddarwish.githubusers.ui.components.CenteredText
import com.mahmouddarwish.githubusers.ui.components.GithubUsersList
import com.mahmouddarwish.githubusers.ui.theme.GitHubUsersCustomTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class DetailsActivity : ComponentActivity() {
    private val viewModel by viewModels<DetailsViewModel>()

    @Inject
    lateinit var myTheme: GitHubUsersCustomTheme

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setUser(extractPassedGithubUser())

        setContent {
            val detailsUIState: DetailsViewModel.DetailsUIState by viewModel.detailsUIStateFlow
                .collectAsState(initial = DetailsViewModel.DetailsUIState.Loading)

            myTheme.GithubUsersTheme {
                DetailsScreen(detailsUIState)
            }
        }
    }

    @Composable
    private fun DetailsScreen(detailsUIState: DetailsViewModel.DetailsUIState) =
        when (detailsUIState) {
            is DetailsViewModel.DetailsUIState.Error -> CenteredText(text = detailsUIState.message)
            is DetailsViewModel.DetailsUIState.Success -> DetailsScreenContent(detailsUIState)
            DetailsViewModel.DetailsUIState.Loading -> CenteredLoadingMessageWithIndicator(
                Modifier.background(MaterialTheme.colors.surface)
            )
        }


    @Composable
    private fun DetailsScreenContent(detailsUIState: DetailsViewModel.DetailsUIState.Success) {
        val user = detailsUIState.gitHubUserDetails

        val followersUIState by viewModel.getFollowers(user.login)
            .collectAsState(initial = DetailsViewModel.PeopleUIState.Loading)

        val followingUIState by viewModel.getFollowing(user.login)
            .collectAsState(initial = DetailsViewModel.PeopleUIState.Loading)

        Scaffold(
            topBar = {
                DetailsActionBar(githubUserDetails = user)
            },
        ) { paddingValues -> /* These values are used so that the content wouldn't be behind
                    the bottomBar for example */
            DetailsScreenScaffoldContent(
                paddingValues,
                user,
                followingUIState,
                followersUIState
            )
        }
    }


    @OptIn(ExperimentalPagerApi::class)
    @Composable
    private fun DetailsScreenScaffoldContent(
        paddingValues: PaddingValues,
        githubUser: GitHubUserDetails,
        followingUIState: DetailsViewModel.PeopleUIState,
        followersUIState: DetailsViewModel.PeopleUIState,
    ) {
        val tabs = remember {
            listOf(
                DetailsScreenTabs.Details,
                DetailsScreenTabs.Following,
                DetailsScreenTabs.Followers
            )
        }

        Column(Modifier.padding(paddingValues)) {
            val pagerState = rememberPagerState(0)

            TabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                    )
                }
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        text = { Text(tab.name) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            lifecycleScope.launch {
                                pagerState.scrollToPage(
                                    index
                                )
                            }
                        },
                    )
                }
            }

            HorizontalPager(
                count = 3,
                verticalAlignment = Alignment.Top,
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
            ) { tabIndex: Int ->
                when (tabs[tabIndex]) {
                    DetailsScreenTabs.Details -> UserDetailsTabPage(githubUserDetails = githubUser)
                    DetailsScreenTabs.Followers -> FollowersTabPage(followersUIState = followersUIState)
                    DetailsScreenTabs.Following -> FollowingTabPage(followingUIState = followingUIState)
                }
            }
        }
    }

    @Composable
    private fun FollowersTabPage(
        modifier: Modifier = Modifier,
        followersUIState: DetailsViewModel.PeopleUIState,
    ) = when (followersUIState) {
        is DetailsViewModel.PeopleUIState.Error -> CenteredText(
            text = followersUIState.message,
            modifier = modifier
        )
        is DetailsViewModel.PeopleUIState.Success -> GithubUsersList(
            users = followersUIState.users,
            modifier = modifier.fillMaxSize()
        )
        DetailsViewModel.PeopleUIState.Loading -> CenteredLoadingMessageWithIndicator(modifier)
    }

    @Composable
    private fun FollowingTabPage(
        modifier: Modifier = Modifier,
        followingUIState: DetailsViewModel.PeopleUIState,
    ) {
        when (followingUIState) {
            is DetailsViewModel.PeopleUIState.Error -> {
                CenteredText(text = followingUIState.message, modifier = modifier)
            }
            is DetailsViewModel.PeopleUIState.Success -> {
                GithubUsersList(users = followingUIState.users, modifier = modifier.fillMaxSize())
            }
            DetailsViewModel.PeopleUIState.Loading -> {
                CenteredLoadingMessageWithIndicator(modifier)
            }
        }
    }

    @Composable
    private fun UserDetailsTabPage(
        modifier: Modifier = Modifier,
        githubUserDetails: GitHubUserDetails,
    ) {
        val isFavorite by viewModel.isUserAFavorite.collectAsState(initial = false)

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    viewModel.toggleFavoriteStatus(githubUserDetails, isFavorite)
                    Toast.makeText(
                        this,
                        if (isFavorite) resources.getString(R.string.removing_favorite_toast_message)
                        else resources.getString(R.string.adding_favorite_toast_message),
                        Toast.LENGTH_SHORT
                    ).show()
                }) {
                    val icon =
                        if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder

                    Icon(icon,
                        contentDescription = stringResource(R.string.floating_action_button_icon_description))
                }
            }
        ) { paddingValues ->
            Column(
                verticalArrangement = spacedBy(4.dp),
                modifier = modifier
                    .verticalScroll(rememberScrollState())
                    .padding(paddingValues)
                    .padding(bottom = 96.dp)
                    .padding(8.dp)
            ) {
                /**
                 * The user image
                 * */
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                ) {
                    CoilImage(
                        url = githubUserDetails.avatarUrl,
                        imageDescription = getString(R.string.avatar_image_description),
                        modifier = modifier
                    )
                }
                /**
                 * The name of the user
                 * */
                Text(
                    text = githubUserDetails.name.orDefault(stringResource(id = R.string.undefined_by_user)),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.h4
                )
                /**
                 * The user ID
                 * */
                Text(
                    text = githubUserDetails.login,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                /**
                 * The user details card
                 * */
                Card(elevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        /**
                         * Following and followers row
                         * */
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = spacedBy(4.dp),
                            modifier = Modifier
                                .wrapContentHeight()
                        ) {
                            Icon(
                                Icons.Outlined.People,
                                contentDescription = stringResource(R.string.following_and_the_followers_icon_description)
                            )
                            Text(
                                text = stringResource(
                                    id = R.string.followers_and_number,
                                    githubUserDetails.followers
                                )
                            )
                            Box(
                                Modifier
                                    .clip(CircleShape), content = {
                                    Surface(
                                        color = Color.Black.copy(alpha = 0.5f),
                                        modifier = Modifier
                                            .size(4.dp)
                                    ) {}
                                })
                            Text(
                                text = stringResource(
                                    id = R.string.following_and_number,
                                    githubUserDetails.following
                                )
                            )
                        }
                        /**
                         * Repositories
                         * */
                        Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.repositories))
                            }
                            append(githubUserDetails.publicRepos.toString())
                        })
                        /**
                         * Gists
                         * */
                        Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.gists))
                            }
                            append(githubUserDetails.publicGists.toString())
                        })
                        /**
                         * Company
                         * */
                        Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.company))
                            }
                            append(githubUserDetails.company.orDefault(stringResource(id = R.string.undefined_by_user)))
                        })
                        /**
                         * Location
                         * */
                        Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.location))
                            }
                            append(githubUserDetails.location.orDefault(stringResource(R.string.undefined_by_user)))
                        })
                        /**
                         * Joining date
                         * */
                        Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.joined))
                            }
                            append(githubUserDetails.createdAt.substring(0..9))
                        })
                        /**
                         * Bio
                         * */
                        Text(text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(stringResource(R.string.bio))
                            }
                            append(githubUserDetails.bio.orDefault(stringResource(id = R.string.undefined_by_user)))
                        })
                    }
                }
                /**
                 * Visit profile button
                 * */
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        openWebPage(githubUserDetails.htmlUrl)
                    }
                ) {
                    Text(
                        text = stringResource(R.string.visit_profile),
                        Modifier.padding(end = 8.dp),
                        color = MaterialTheme.colors.onPrimary
                    )
                    Icon(Icons.Default.OpenInBrowser, "")
                }
            }
        }
    }


    /**
     * The action bar of the details screen
     * */
    @Composable
    private fun DetailsActionBar(
        modifier: Modifier = Modifier,
        githubUserDetails: GitHubUserDetails,
    ) {
        TopAppBar(
            modifier = modifier,
            title = { Text(text = githubUserDetails.name.orDefault(stringResource(id = R.string.undefined_by_user))) },
            navigationIcon = {
                IconButton(onClick = {
                    // ending the activity and going back to the previous one.
                    navigateUp()
                }) {
                    Icon(Icons.Default.ArrowBack,
                        contentDescription = stringResource(R.string.navigating_back_icon))
                }
            },
            actions = {
                IconButton(onClick = {
                    // Share the user profile link
                    shareText(githubUserDetails.htmlUrl)
                }) {
                    Icon(Icons.Default.Share,
                        contentDescription = stringResource(R.string.share_icon))
                }
            }
        )
    }

    /**
     * This sealed class contains the representation of the tabs the the details screen has
     * */
    sealed class DetailsScreenTabs(val name: String) {
        object Details : DetailsScreenTabs("Details")
        object Following : DetailsScreenTabs("Following")
        object Followers : DetailsScreenTabs("Followers")
    }

    companion object {

        /*
        * It's made private since there is a method provided for creating an intent to start this
        * activity with the extra needed data that's identified by this key passed to it
        * */
        private const val USER_INTENT_KEY = "user_intent_key"

        /*
        * To avoid having to know the key to create an intent for this activity with the extra data,
        * this method is provided
        * */
        fun createIntentWithGithubUserData(
            context: Context,
            githubUserDetails: GitHubUser,
        ): Intent {
            return Intent(context, DetailsActivity::class.java).apply {
                putExtra(USER_INTENT_KEY, githubUserDetails)
            }
        }

        /*
        * Just extracts the passed GitHubUser object and returns it
        * */
        fun DetailsActivity.extractPassedGithubUser(): GitHubUser? =
            intent.getParcelableExtra(USER_INTENT_KEY)
    }
}