package solutions.vjk.catalist.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.SharedFlow
import solutions.vjk.catalist.models.Assignee
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.state.EditItemState
import solutions.vjk.catalist.ui.theme.gray1
import solutions.vjk.catalist.widgets.*
import java.util.*

@Composable
fun EditItemPage(
    state: EditItemState,
    navController: NavController,
    doSetAssignee: (Assignee?) -> Unit,
    doSetCategory: (Category) -> Unit,
    doSetName: (String) -> Unit,
    doSetDueDate: (Boolean, Calendar?) -> Unit,
    doSetBudget: (Boolean, Int, Int) -> Unit,
    doToggleInProgress: () -> Unit,
    doToggleComplete: () -> Unit,
    doSave: () -> Unit,
    doDelete: () -> Unit,
    toastMessage: SharedFlow<String>
) {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val name = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        toastMessage
            .collect { message ->
                scaffoldState.snackbarHostState.showSnackbar(message)
            }
    }

    LaunchedEffect(state.name) {
        name.value = state.name
    }

    fun deleteItem() {
        doDelete()
        navController.navigate("root")
    }

    fun changeName(value: String) {
        name.value = value
    }

    fun save() {
        doSetName(name.value)
        doSave()
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
                        text = "Edit an Item",
                        scaffoldState = scaffoldState,
                        scope = scope,
                        navController = navController,
                        fontSize = 24.sp,
                        additionalButton = {
                            IconButton(onClick = { deleteItem() }) {
                                Icon(Icons.Default.Delete, "Delete this item")
                            }
                        }
                    )
                },
                floatingActionButton = {
                    EditFAB(click = { save() })
                },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.End,
                bottomBar = {
                    StandardBottomBar(text = "Edit Item")
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
                    Card(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxWidth()
                    ) {
                        Column {
                            NameWidget(name = name.value, valueChanged = { v -> changeName(v) })
                            Divider(color = gray1, modifier = Modifier.padding(vertical = 4.dp))
                            SelectCategoryWidget(
                                displayedCategoryName = state.displayedCategoryName,
                                doSetCategory = doSetCategory,
                                categories = state.categories
                            )
                            Divider(color = gray1, modifier = Modifier.padding(vertical = 4.dp))
                            SelectAssigneeWidget(
                                displayedAssigneeName = state.displayedAssigneeName,
                                doSetAssignee = doSetAssignee,
                                assignees = state.assignees
                            )
                            Divider(color = gray1, modifier = Modifier.padding(vertical = 4.dp))
                            DueDateWidget(
                                hasDueDate = state.hasDueDate,
                                dueDate = state.dueDate,
                                doSetDueDate = doSetDueDate
                            )
                            Divider(color = gray1, modifier = Modifier.padding(vertical = 4.dp))
                            BudgetWidget(
                                hasBudget = state.hasBudget,
                                budget = state.budget,
                                spent = state.spent,
                                doSetBudget = doSetBudget
                            )
                            Divider(color = gray1, modifier = Modifier.padding(vertical = 4.dp))
                            ItemToggleWidget(
                                isInProgress = state.inProgress,
                                isComplete = state.complete,
                                toggleInProgress = doToggleInProgress,
                                toggleComplete = doToggleComplete
                            )
                        }
                    }
                }
            )
        }
        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                modifier = Modifier.align(Alignment.BottomCenter),
                color = MaterialTheme.colorScheme.error
            )
        }
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}