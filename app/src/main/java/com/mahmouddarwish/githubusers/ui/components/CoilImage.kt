package com.mahmouddarwish.githubusers.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import coil.size.OriginalSize

/**
 * Loads an image using the url provided or shows a loading indicator if the image is still
 * being loaded
 * */
@OptIn(ExperimentalCoilApi::class)
@Composable
fun CoilImage(url: String, imageDescription: String, modifier: Modifier = Modifier) {
    val painter = rememberImagePainter(data = url) {
        size(OriginalSize)
    }

    if (isImageNotReady(painter)) {
        // show a loading indicator while the image is being loaded
        CircularProgressIndicator(modifier)
    } else {
        Surface(modifier.shadow(4.dp, CircleShape)) {
            Image(
                painter = painter,
                contentDescription = imageDescription,
                modifier = Modifier
                    .fillMaxHeight(),
                contentScale = ContentScale.FillHeight
            )
        }
    }
}

@ExperimentalCoilApi
private fun isImageNotReady(painter: ImagePainter) = (painter.state !is ImagePainter.State.Success
        && painter.state is ImagePainter.State.Loading
        || painter.state is ImagePainter.State.Empty)