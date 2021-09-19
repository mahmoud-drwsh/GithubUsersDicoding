package com.mahmouddarwish.githubusers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable

/**
 * Used in the repository class to help manage the different states with much greater structure
 * than without
 * */
sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    class Error(val message: String) : Resource<Nothing>()
    class Loaded<T>(val users: T) : Resource<T>()
}

/**
 * The Constants used throughout the app
 * */
object Constants {
    const val UNKNOWN_ERROR_MESSAGE: String = "An unknown error occurred"
}

/**
 * For opening a url in a browser
 * */
fun Context.openWebPage(url: String) {
    val webpage: Uri = Uri.parse(url)
    val intent = Intent(Intent.ACTION_VIEW, webpage)
    try {
        startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(this, "Unable to open the webpage", Toast.LENGTH_SHORT).show()
    }
}

/**
 * For sharing text. In the case of this app it's used for sharing a user's profile link
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
        Toast.makeText(this, "Unable to share due to an error", Toast.LENGTH_SHORT).show()
    }
}

/**
 * Returns the string if it's not `null` or empty else return the default string provided.
 * */
fun String?.orDefault(
    default: String
) = if (!this.isNullOrBlank()) this else default