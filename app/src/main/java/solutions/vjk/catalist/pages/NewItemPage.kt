package solutions.vjk.catalist.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.SharedFlow
import solutions.vjk.catalist.models.Assignee
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.state.NewItemState
import solutions.vjk.catalist.ui.theme.gray1
import solutions.vjk.catalist.widgets.*
import java.util.*

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewItemPage(
    state: NewItemState,
    navController: NavController,
    doSetAssignee: (Assignee) -> Unit,
    doSetCategory: (Category) -> Unit,
    doSetName: (String) -> Unit,
    doSetDueDate: (Boolean, Calendar?) -> Unit,
    doSetBudget: (Boolean, Int, Int) -> Unit,
    doToggleInProgress: () -> Unit,
    doToggleComplete: () -> Unit,
    doSave: () -> Unit,
    toastMessage: SharedFlow<String>
) {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        toastMessage
            .collect { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!state.isLoading) {
            Scaffold(
                modifier = Modifier
                    .padding(all = 16.dp)
                    .fillMaxWidth(),
                scaffoldState = scaffoldState,
                topBar = {
                    StandardTopBar(
                        text = "Create an Item",
                        scaffoldState = scaffoldState,
                        scope = scope,
                        navController = navController,
                        additionalButton = {
                            IconButton(
                                onClick = {
                                    keyboardController?.hide()
                                    doSave()
                                }
                            ) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Save Changes",
                                )
                            }
                        }
                    )
                },
                bottomBar = {
                    StandardBottomBar(text = "Create Item")
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
                        modifier = Modifier
                            .padding(it)
                            .fillMaxWidth()
                    ) {
                        Column {
                            NameWidget(name = state.name, valueChanged = doSetName)
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