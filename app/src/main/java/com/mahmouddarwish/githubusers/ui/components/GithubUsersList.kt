package com.mahmouddarwish.githubusers.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mahmouddarwish.githubusers.R
import com.mahmouddarwish.githubusers.domain.models.GitHubUser

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
        items(users) { user ->
            UserRowItem(gitHubUser = user, onItemClick = onUserRowItemClick)
        }

        item {
            ListFooter(users.size)
        }
    }
}

@Composable
private fun ListFooter(displayedUsersCount: Int) {
    Text(
        text = stringResource(id = R.string.number_of_displayed_users, displayedUsersCount),
        Modifier.padding(8.dp)
    )
}