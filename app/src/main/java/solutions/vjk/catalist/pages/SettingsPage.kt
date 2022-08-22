package solutions.vjk.catalist.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import solutions.vjk.catalist.state.SettingsState
import solutions.vjk.catalist.ui.theme.gray3
import solutions.vjk.catalist.ui.theme.gray4
import solutions.vjk.catalist.ui.theme.green2
import solutions.vjk.catalist.ui.theme.green3
import solutions.vjk.catalist.widgets.NavMenu
import solutions.vjk.catalist.widgets.SettingsToggle
import solutions.vjk.catalist.widgets.StandardBottomBar
import solutions.vjk.catalist.widgets.StandardTopBar

@Composable
fun SettingsPage(
    state: SettingsState,
    navController: NavController,
    doToggleComplete: () -> Unit
) {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!state.isLoading) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState,
                topBar = {
                    StandardTopBar(
                        text = "Settings",
                        scaffoldState = scaffoldState,
                        scope = scope,
                        navController = navController
                    )
                },
                bottomBar = {
                    StandardBottomBar(text = "App Settings")
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
                    Card(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column {
                            SettingsToggle(
                                text = "Delete on Complete",
                                initialValue = state.settings.deleteOnComplete,
                                doToggle = doToggleComplete,
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = green3,
                                    uncheckedThumbColor = gray4,
                                    checkedTrackColor = green2,
                                    uncheckedTrackColor = gray3
                                ),
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            )
        }
        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}