package com.mahmouddarwish.githubusers.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.mahmouddarwish.githubusers.data.repos.UILightModeRepo
import javax.inject.Inject
import javax.inject.Singleton

/*
* The colors used here are custom ones
* */
private val DarkColorPalette = darkColors(
    primary = primaryDarkColor,
    secondary = secondaryDarkColor,
    onPrimary = primaryTextColor,
    onSecondary = secondaryTextColor
)

/*
* The colors used here are custom ones
* */
private val LightColorPalette = lightColors(
    primary = primaryColor,
    primaryVariant = primaryLightColor,
    secondary = secondaryColor,
    secondaryVariant = secondaryLightColor,
    onPrimary = primaryTextColor,
    onSecondary = secondaryTextColor
)

/**
 * This class was created for the purpose of providing a single source of truth for
 * the light mode of the UI
 * */
@Singleton
class GitHubUsersCustomTheme @Inject constructor(
    private val uiModeRepo: UILightModeRepo
) {
    val isDarkUIMode = uiModeRepo.isDarkUIMode

    @Composable
    fun GithubUsersTheme(
        content: @Composable () -> Unit
    ) {
        val darkTheme by uiModeRepo.isDarkUIMode.collectAsState(initial = false)

        val colors = if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        }

        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}