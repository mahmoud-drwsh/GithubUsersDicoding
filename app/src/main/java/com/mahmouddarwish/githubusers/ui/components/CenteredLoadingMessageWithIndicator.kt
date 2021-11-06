package com.mahmouddarwish.githubusers.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mahmouddarwish.githubusers.R


@Composable
fun CenteredLoadingMessageWithIndicator(modifier: Modifier = Modifier) {
    CenteredContent(modifier) {
        CircularProgressIndicator(modifier = Modifier.size(32.dp),
            color = MaterialTheme.colors.secondary)
        Text(stringResource(R.string.loading), color = MaterialTheme.colors.onSurface)
    }
}
