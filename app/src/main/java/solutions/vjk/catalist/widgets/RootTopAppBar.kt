package solutions.vjk.catalist.widgets

import android.util.Log
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ScaffoldState
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import solutions.vjk.catalist.ui.theme.yellow1

@Composable
fun RootTopAppBar(
    title: @Composable (() -> Unit),
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    dueCount: Int = 0,
    overdueCount: Int = 0,
    showOverdue: () -> Unit
) {
    return TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.secondary,
        title = title,
        actions = {
            if (overdueCount > 0) {
                IconButton(
                    onClick = showOverdue
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "There are overdue items",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            } else {
                if (dueCount > 0) {
                    IconButton(
                        onClick = showOverdue
                    ) {
                        Icon(
                            Icons.Default.DateRange,
                            contentDescription = "Items are due within seven days",
                            tint = yellow1
                        )
                    }
                }
            }
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