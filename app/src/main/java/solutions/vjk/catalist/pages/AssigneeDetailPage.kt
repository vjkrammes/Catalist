package solutions.vjk.catalist.pages

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.SharedFlow
import solutions.vjk.catalist.state.AssigneeDetailState
import solutions.vjk.catalist.widgets.*

@Composable
fun AssigneeDetailPage(
    state: AssigneeDetailState,
    navController: NavController,
    doClearAssignment: (Int) -> Unit,
    doClearAllAssignments: () -> Unit,
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

    fun clearAllAssignments() {
        doClearAllAssignments()
        navController.navigate("assignees")
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
                        text = state.assignee.name,
                        scaffoldState = scaffoldState,
                        scope = scope,
                        navController = navController
                    )
                },
                bottomBar = {
                    StandardBottomBar(text = "Assignee Details")
                },
                floatingActionButton = {
                    StandardFAB(
                        text = "Clear All",
                        icon = Icons.Default.Clear,
                        click = { clearAllAssignments() },
                        contentDescription = "Clear All Assignments"
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
                    if (state.assignedItems.isEmpty()) {
                        Text(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxWidth()
                                .padding(top = 60.dp),
                            text = "No Assigned Items",
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Column(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxWidth(),
                        ) {
                            Text(
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .fillMaxWidth(),
                                text = "Assigned Items",
                                textAlign = TextAlign.Center
                            )
                            Divider(modifier = Modifier.padding(vertical = 4.dp))
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(state.assignedItems) { item ->
                                    ItemCard(
                                        item = item,
                                        onButtonClick = { doClearAssignment(item.id) },
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