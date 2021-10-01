package com.mahmouddarwish.githubusers.ui.components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Fills max available space and shows a message in the center
 * */
@Composable
fun CenteredText(
    text: String,
    modifier: Modifier = Modifier
) {
    CenteredContent(modifier) {
        Text(text)
    }
}