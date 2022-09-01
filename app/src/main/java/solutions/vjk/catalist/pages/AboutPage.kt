package solutions.vjk.catalist.pages

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import solutions.vjk.catalist.App
import solutions.vjk.catalist.PROGRAM_VERSION
import solutions.vjk.catalist.R
import solutions.vjk.catalist.infrastructure.toDateString
import solutions.vjk.catalist.infrastructure.toInt
import solutions.vjk.catalist.models.LinkTextItem
import solutions.vjk.catalist.widgets.LinkText
import solutions.vjk.catalist.widgets.NavMenu
import solutions.vjk.catalist.widgets.StandardBottomBar
import solutions.vjk.catalist.widgets.StandardTopBar
import java.util.*

private const val VJK_SOLUTIONS_URL: String = "https://vjk.solutions"
private const val KOTLIN_URL: String = "https://kotlinlang.org"
private const val COMPOSE_URL: String = "https://developer.android.com/jetpack/compose"
private const val SQLITE_URL: String = "https://www.sqlite.org/index.html"

@Composable
fun AboutPage(
    navController: NavController,
    context: Context
) {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val buildTimestamp = Calendar.getInstance()
    buildTimestamp.timeInMillis = App.buildTime

    fun launchUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }

    val items: Map<String, LinkTextItem> = mapOf(
        "Program Version" to LinkTextItem(text = String.format("%.2f", PROGRAM_VERSION)),
        "Build Date" to LinkTextItem(text = buildTimestamp.toInt().toDateString()),
        "Author" to LinkTextItem(text = "V. James Krammes"),
        "Company" to LinkTextItem(text = "VJK Solutions, LLC"),
        "URL" to LinkTextItem(
            text = VJK_SOLUTIONS_URL,
            tag = "VJKURL",
            annotation = VJK_SOLUTIONS_URL,
            onClick = {
                launchUrl(VJK_SOLUTIONS_URL)
            }
        )
    )
    val narrativeItems: List<LinkTextItem> = listOf(
        LinkTextItem(text = "Catalist is a demonstration app for Android. It is written in "),
        LinkTextItem(
            text = "Kotlin",
            tag = "sourceLanguage",
            annotation = KOTLIN_URL,
            onClick = {
                launchUrl(KOTLIN_URL)
            }
        ),
        LinkTextItem(text = " using "),
        LinkTextItem(
            text = "Jetpack Compose",
            tag = "UIFramework",
            annotation = COMPOSE_URL,
            onClick = {
                launchUrl(COMPOSE_URL)
            }
        ),
        LinkTextItem(text = " for its user interface. It is a categorized to-do app, with a few "),
        LinkTextItem(text = "twists: it allows the user to assign tasks to others, set "),
        LinkTextItem(text = "due dates, or even attach a budget. Multiple lists can be maintained "),
        LinkTextItem(text = "with separate categories per list. Categories are optional, and "),
        LinkTextItem(text = "categorized and uncategorized items can be mixed within the same list. "),
        LinkTextItem(text = "Data is stored using "),
        LinkTextItem(
            text = "SQLite",
            tag = "Database",
            annotation = SQLITE_URL,
            onClick = {
                launchUrl(SQLITE_URL)
            }
        ),
        LinkTextItem(text = ".")
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            StandardTopBar(
                text = stringResource(R.string.app_name)
                    .plus(" version ")
                    .plus(String.format("%.2f", PROGRAM_VERSION)),
                scaffoldState = scaffoldState,
                scope = scope,
                navController = navController
            )
        },
        bottomBar = {
            StandardBottomBar(text = "About Catalist")
        },
        drawerContent = {
            NavMenu(
                navController = navController,
                scaffoldState = scaffoldState,
                scope = scope
            )
        },
        drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    elevation = 24.dp
                ) {
                    LinkText(
                        modifier = Modifier.padding(all = 12.dp),
                        linkTextItems = narrativeItems
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                for (item in items) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = item.key,
                            modifier = Modifier.weight(0.5f),
                            fontWeight = FontWeight.Bold
                        )
                        LinkText(item = item.value)
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }
            }
        }
    )
}