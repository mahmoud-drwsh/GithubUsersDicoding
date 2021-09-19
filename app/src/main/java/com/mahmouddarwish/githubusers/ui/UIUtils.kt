package com.mahmouddarwish.githubusers.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.OriginalSize

/**
 * Fills max available space and shows a message in the center
 * */
@Composable
fun CenteredText(text: String) {
    CenteredContent {
        Text(text)
    }
}

@Composable
fun CenteredContent(content: @Composable ColumnScope.() -> Unit) =
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )

/**
 * Loads an image using the url provided or shows a loading indicator if the image is still being loaded
 * */
@OptIn(ExperimentalCoilApi::class)
@Composable
fun CustomCoilImage(url: String) {
    val painter = rememberImagePainter(data = url) {
        size(OriginalSize)
    }

    if (
        painter.state !is ImagePainter.State.Success
        && painter.state is ImagePainter.State.Loading
        || painter.state is ImagePainter.State.Empty
    ) {
        CircularProgressIndicator()
    } else {
        Image(
            painter = painter,
            contentDescription = "",
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxHeight(),
            contentScale = ContentScale.FillHeight
        )
    }
}