package solutions.vjk.catalist.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.SharedFlow
import solutions.vjk.catalist.models.Item
import solutions.vjk.catalist.state.AllItemsState
import solutions.vjk.catalist.widgets.ItemCard
import solutions.vjk.catalist.widgets.NavMenu
import solutions.vjk.catalist.widgets.StandardBottomBar
import solutions.vjk.catalist.widgets.StandardTopBar

@Composable
fun AllItemsPage(
    state: AllItemsState,
    navController: NavController,
    doDelete: (Item) -> Unit,
    doComplete: (Item) -> Unit,
    doEdit: (Item) -> Unit,
    toastMessage: SharedFlow<String>
) {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        toastMessage
            .collect { message ->
                scaffoldState.snackbarHostState.showSnackbar(message)
            }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (!state.isLoading) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState,
                topBar = {
                    StandardTopBar(
                        text = "All Items",
                        scaffoldState = scaffoldState,
                        scope = scope,
                        navController = navController
                    )
                },
                bottomBar = {
                    StandardBottomBar(text = "Show All Items")
                },
                drawerContent = {
                    NavMenu(
                        navController = navController,
                        scaffoldState = scaffoldState,
                        scope = scope
                    )
                },
                snackbarHost = {
                    SnackbarHost(hostState = scaffoldState.snackbarHostState)
                },
                content = {
                    Surface(
                        modifier = Modifier.padding(it)
                    ) {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(state.items) { item ->
                                ItemCard(
                                    item = item,
                                    onButtonClick = if (item.complete) doDelete else doComplete,
                                    onItemClick = doEdit,
                                )
                            }
                        }
                    }
                }
            )
        }
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}