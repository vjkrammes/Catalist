package solutions.vjk.catalist.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.SharedFlow
import solutions.vjk.catalist.state.CategoryDetailState
import solutions.vjk.catalist.widgets.*

@Composable
fun CategoryDetailPage(
    state: CategoryDetailState,
    navController: NavController,
    doDeleteItem: (Int) -> Unit,
    doDeleteAllItems: () -> Unit,
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

    fun deleteAllItems() {
        doDeleteAllItems()
        navController.navigate("categories")
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!state.isLoading) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState,
                topBar = {
                    StandardTopBar(
                        text = state.category.name,
                        scaffoldState = scaffoldState,
                        scope = scope,
                        navController = navController
                    )
                },
                bottomBar = {
                    StandardBottomBar(text = "Category Details")
                },
                floatingActionButton = {
                    StandardFAB(
                        text = "Delete All",
                        icon = Icons.Default.Delete,
                        click = { deleteAllItems() },
                        contentDescription = "Delete All Items"
                    )
                },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.End,
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
                    if (state.items.isEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            text = "No Items Found",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxWidth()
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth(),
                                text = "Items in Category",
                                textAlign = TextAlign.Center
                            )
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(state.items) { item ->
                                    ItemCard(
                                        item = item,
                                        onButtonClick = { doDeleteItem(item.id) },
                                        onItemClick = {
                                            navController.navigate("edit/${item.id}")
                                        }
                                    )
                                }
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