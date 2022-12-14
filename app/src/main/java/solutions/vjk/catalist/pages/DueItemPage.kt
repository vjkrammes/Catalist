package solutions.vjk.catalist.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.SharedFlow
import solutions.vjk.catalist.state.DueItemState
import solutions.vjk.catalist.widgets.DueItemCard
import solutions.vjk.catalist.widgets.NavMenu
import solutions.vjk.catalist.widgets.StandardBottomBar
import solutions.vjk.catalist.widgets.StandardTopBar

@Composable
fun DueItemPage(
    state: DueItemState,
    navController: NavController,
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
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!state.isLoading) {
            Scaffold(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                scaffoldState = scaffoldState,
                topBar = {
                    StandardTopBar(
                        text = "(Over)Due Items",
                        scaffoldState = scaffoldState,
                        scope = scope,
                        navController = navController,
                        fontSize = 24.sp
                    )
                },
                bottomBar = {
                    StandardBottomBar(text = "Due/Overdue Items")
                },
                drawerContent = {
                    NavMenu(
                        navController = navController,
                        scaffoldState = scaffoldState,
                        scope = scope
                    )
                },
                drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp),
                snackbarHost = {
                    SnackbarHost(hostState = scaffoldState.snackbarHostState)
                },
                content = {
                    Surface(
                        modifier = Modifier.padding(it)
                    ) {
                        LazyColumn {
                            items(state.items) { item ->
                                DueItemCard(
                                    item = item,
                                    onClick = { navController.navigate("edit/${item.id}") }
                                )
                                Divider()
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