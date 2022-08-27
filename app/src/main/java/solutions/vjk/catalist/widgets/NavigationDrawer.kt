package solutions.vjk.catalist.widgets

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Divider
import androidx.compose.material.ScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import solutions.vjk.catalist.models.NavigationMenuItem

//
// code based on this article: https://johncodeos.com/how-to-create-a-navigation-drawer-with-jetpack-compose/
//

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NavigationDrawer(
    items: List<NavigationMenuItem>,
    navController: NavController,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    title: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    return Column(
        modifier = modifier.then(Modifier.fillMaxWidth())
    ) {
        if (title != null) {
            title()
            Divider(modifier = Modifier.height(4.dp))
        }
        items.forEach { item ->
            if (item.text == "divider") {
                Divider(color = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f))
            } else {
                NavigationDrawerItem(
                    item = item,
                    selected = false,
                    onItemClick = {
                        keyboardController?.hide()
                        navController.navigate(item.route) {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = false
                                }
                            }
                            launchSingleTop = true
                            restoreState = false
                        }
                        scope.launch {
                            scaffoldState.drawerState.close()
                        }
                    }
                )
            }
        }
    }
}