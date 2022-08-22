package solutions.vjk.catalist.widgets

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun RootTopAppBar(
    title: @Composable (() -> Unit),
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
) {
    return TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.secondary,
        title = title,
        actions = {
            IconButton(
                onClick = {
                    scope.launch {
                        if (scaffoldState.drawerState.isClosed) {
                            scaffoldState.drawerState.open()
                        } else {
                            scaffoldState.drawerState.close()
                        }
                    }
                }
            ) {
                Icon(Icons.Default.Menu, "Navigation Menu")
            }
        }
    )
}