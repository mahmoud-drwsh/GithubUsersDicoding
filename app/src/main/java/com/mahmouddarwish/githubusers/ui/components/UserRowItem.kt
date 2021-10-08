package com.mahmouddarwish.githubusers.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.mahmouddarwish.githubusers.R
import com.mahmouddarwish.githubusers.domain.models.GitHubUser

@Composable
fun UserRowItem(
    gitHubUser: GitHubUser,
    modifier: Modifier = Modifier,
    onItemClick: (GitHubUser) -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clickable {
                onItemClick(gitHubUser)
            }
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
                    imageDescription = stringResource(R.string.avatar_image_description)
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