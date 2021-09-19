package com.mahmouddarwish.githubusers.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.People
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.mahmouddarwish.githubusers.model.GithubUserData
import com.mahmouddarwish.githubusers.openWebPage
import com.mahmouddarwish.githubusers.orDefault
import com.mahmouddarwish.githubusers.shareText
import com.mahmouddarwish.githubusers.ui.theme.GithubUsersTheme

class DetailsActivity : ComponentActivity() {

    companion object {
        const val USER_INTENT_KEY = "user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userData = extractUserData() as GithubUserData
        setContent {
            GithubUsersTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text(text = "${userData.name}") },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = "")
                                }
                            },
                            actions = {
                                IconButton(onClick = {
                                    shareText(userData.htmlUrl)
                                }) {
                                    Icon(Icons.Default.Share, contentDescription = "")
                                }
                            })
                    }
                ) { paddingValues ->
                    UserDetails(githubUserData = userData, paddingValues = paddingValues) {
                        openWebPage(userData.htmlUrl)
                    }
                }
            }
        }
    }

    private fun extractUserData(): GithubUserData? {
        return intent.getParcelableExtra<GithubUserData>(USER_INTENT_KEY)
    }

}

@Composable
fun UserDetails(
    githubUserData: GithubUserData,
    paddingValues: PaddingValues,
    onOpenProfileButtonClick: () -> Unit
) {
    Column(
        verticalArrangement = spacedBy(4.dp),
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(paddingValues)
            .padding(8.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            CustomCoilImage(url = githubUserData.avatarUrl)
        }
        Text(
            text = githubUserData.name.toString(),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.h4
        )
        Text(
            text = githubUserData.login,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Card(elevation = 8.dp, modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = spacedBy(4.dp),
                    modifier = Modifier
                        .wrapContentHeight()
                ) {
                    Icon(Icons.Outlined.People, contentDescription = "")
                    Text(text = "${githubUserData.followers} followers")
                    Box(
                        Modifier
                            .clip(CircleShape), content = {
                            Surface(
                                color = Color.Black.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .size(4.dp)
                            ) {}
                        })
                    Text(text = "${githubUserData.followers} following")
                }
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Repositories: ")
                    }
                    append(githubUserData.publicRepos.toString())
                })
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Gists: ")
                    }
                    append(githubUserData.publicGists.toString())
                })
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Company: ")
                    }
                    append(githubUserData.company.orDefault("Not Defined by the user"))
                })
                Text(text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Location: ")
                    }
                    append(githubUserData.location.orDefault("Not Defined by the user"))
                })
            }
        }
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            onOpenProfileButtonClick()
        }) {
            Text(text = "Open Profile", Modifier.padding(end = 8.dp))
            Icon(Icons.Default.OpenInBrowser, "")
        }
    }
}

