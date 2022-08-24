package solutions.vjk.catalist.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.SharedFlow
import solutions.vjk.catalist.models.Assignee
import solutions.vjk.catalist.state.ManageAssigneesState
import solutions.vjk.catalist.widgets.*

@Composable
fun ManageAssigneesPage(
    state: ManageAssigneesState,
    navController: NavController,
    doNew: (String) -> Unit,
    doDelete: (Assignee) -> Unit,
    doDetails: (Assignee, NavController) -> Unit,
    toastMessage: SharedFlow<String>
) {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val (showInput, setShowInput) = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        toastMessage
            .collect { message ->
                scaffoldState.snackbarHostState.showSnackbar(message)
            }
    }

    fun setUpForNewAssignee() {
        setShowInput(!showInput)
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!state.isLoading) {
            Scaffold(
                modifier = Modifier.fillMaxWidth(),
                scaffoldState = scaffoldState,
                topBar = {
                    AssigneeTopAppBar(
                        showInput = showInput,
                        setShowInput = setShowInput,
                        scaffoldState = scaffoldState,
                        scope = scope,
                        navController = navController,
                        doNew = doNew
                    )
                },
                floatingActionButton = {
                    RootFAB {
                        setUpForNewAssignee()
                    }
                },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.End,
                bottomBar = {
                    StandardBottomBar(text = "Manage Assignees")
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
                    if (state.assignees.isEmpty()) {
                        Text(
                            text = "No Assignees Found",
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                                .padding(top = 60.dp),
                            fontStyle = FontStyle.Italic,
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxWidth()
                        ) {
                            items(state.assignees) { assignee ->
                                AssigneeCard(
                                    assignee = assignee,
                                    assignmentCount =
                                    state.assignments.singleOrNull { a -> a.id == assignee.id }?.count
                                        ?: 0,
                                    onButtonClick = doDelete,
                                    onAssigneeClick = { a ->
                                        doDetails(a, navController)
                                    }
                                )
                                Divider(color = MaterialTheme.colorScheme.onSurface)
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