package com.mahmouddarwish.githubusers.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.mahmouddarwish.githubusers.Constants
import com.mahmouddarwish.githubusers.R
import com.mahmouddarwish.githubusers.data.domain.models.GitHubUser

/**
 * This Composable is used to display a list of GitHubUsers that are received from the API
 * */
@Composable
fun GithubUsersList(
    modifier: Modifier = Modifier,
    users: List<GitHubUser>,
    onUserRowItemClick: (GitHubUser) -> Unit = {}
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp),
        modifier = modifier
    ) {
        items(users) {
            UserRowItem(gitHubUser = it, onItemClick = onUserRowItemClick)
        }

        item {
            Text(
                text = stringResource(id = R.string.number_of_displayed_users, users.size),
                Modifier.padding(8.dp)
            )
        }
    }
}


@Composable
fun UserRowItem(
    modifier: Modifier = Modifier,
    gitHubUser: GitHubUser,
    onItemClick: (GitHubUser) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .clickable {
                onItemClick(gitHubUser)
            },
        elevation = 8.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        ConstraintLayout(Modifier.padding(8.dp)) {
            val (image, username) = createRefs()

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(64.dp)
                    .padding(end = 4.dp)
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
                CoilImage(
                    url = gitHubUser.avatarUrl,
                    imageDescription = Constants.USER_AVATAR_IMAGE_DESCRIPTION
                )
            }

            Text(text = gitHubUser.login,
                modifier = Modifier
                    .constrainAs(username) {
                        start.linkTo(image.end)
                        top.linkTo(image.top)
                        bottom.linkTo(image.bottom)
                    }
                    .fillMaxWidth())
        }
    }

}


/**
 * Fills max available space and shows a message in the center
 * */
@Composable
fun CenteredText(modifier: Modifier = Modifier, text: String) {
    CenteredContent(modifier) {
        Text(text)
    }
}

/**
 * Centers the passed composable in the middle of the maximum available space
 * */
@Composable
fun CenteredContent(
    modifier: Modifier = Modifier,
    content: @Composable (ColumnScope.() -> Unit)
) =
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )

/**
 * Loads an image using the url provided or shows a loading indicator if the image is still
 * being loaded
 * */
@OptIn(ExperimentalCoilApi::class)
@Composable
fun CoilImage(modifier: Modifier = Modifier, url: String, imageDescription: String) {
    val painter = rememberImagePainter(data = url) {
        size(OriginalSize)
    }
    /*
    * checking if the image is ready to be displayed and taking the appropriate action not to
    * confuse the user
    * */
    if (
        painter.state !is ImagePainter.State.Success
        && painter.state is ImagePainter.State.Loading
        || painter.state is ImagePainter.State.Empty
    ) {
        // show a loading indicator while the image is still being loaded
        CircularProgressIndicator(modifier)
    } else {
        Image(
            painter = painter,
            contentDescription = imageDescription,
            modifier = modifier
                .clip(CircleShape)
                .shadow(8.dp, CircleShape)
                .fillMaxHeight(),
            contentScale = ContentScale.FillHeight
        )
    }
}


@Composable
fun CenteredLoadingMessageWithIndicator(modifier: Modifier = Modifier) {
    CenteredContent(modifier) {
        CircularProgressIndicator(modifier = Modifier.size(32.dp))
        Text(stringResource(R.string.loading))
    }
}
