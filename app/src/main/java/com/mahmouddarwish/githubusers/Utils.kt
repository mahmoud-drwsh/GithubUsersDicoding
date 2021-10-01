package com.mahmouddarwish.githubusers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * The Constants used throughout the app
 * */
object Constants {
    /**
     * A generic error message
     * */
    const val USER_AVATAR_IMAGE_DESCRIPTION = "The user's avatar image"
}

/**
 * For opening a url in a browser. In case of an error, a toast is displayed notifying the user
 * */
fun Context.openWebPage(url: String) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    try {
        startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(this, getString(R.string.unable_to_open_webpage), Toast.LENGTH_SHORT).show()
    }
}

/**
 * For sharing text. In the case of this app, it's used for sharing a user's profile link.
 * In case of an error, a toast is displayed notifying the user
 * */
fun Context.shareText(url: String) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, url)
        type = "text/plain"
    }
    try {
        startActivity(sendIntent)
    } catch (e: Exception) {
        Toast.makeText(this, getString(R.string.error_unable_to_share), Toast.LENGTH_SHORT).show()
    }
}

/**
 * Returns the string if it's not `null` or empty else return the default string provided.
 * */
fun String?.orDefault(default: String) = if (!this.isNullOrBlank()) this else default