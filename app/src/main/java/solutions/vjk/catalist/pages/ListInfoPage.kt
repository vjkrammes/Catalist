package solutions.vjk.catalist.pages

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import solutions.vjk.catalist.R
import solutions.vjk.catalist.infrastructure.filterText
import solutions.vjk.catalist.infrastructure.toCurrency
import solutions.vjk.catalist.infrastructure.toDateString
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.models.Item
import solutions.vjk.catalist.models.ToDoList
import solutions.vjk.catalist.state.RenameState
import solutions.vjk.catalist.widgets.*

@Composable
fun ListInfoPage(
    navController: NavController,
    currentList: ToDoList,
    items: List<Item>,
    categories: List<Category>,
    doRename: (String) -> Unit
) {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val state = mutableStateOf(
        RenameState(
            showRename = false,
            newName = currentList.name
        )
    )
    val lora = FontFamily(Font(R.font.lora, FontWeight.Normal))

    var itemCount = 0
    var assignedItemCount = 0
    var completedItemCount = 0
    var dueItemCount = 0
    var budgetedItemCount = 0
    var totalBudget = 0
    if (items.isNotEmpty()) {
        itemCount = items.size
        assignedItemCount = items.count { it.assigneeId != 0 }
        completedItemCount = items.count { it.complete }
        dueItemCount = items.count { it.hasDueDate() }
        budgetedItemCount = items.count { it.budget != 0 }
        totalBudget = items.sumOf { it.budget }
    }
    val categoryCount = categories.size

    val headerItems: Map<String, String> = mapOf(
        "Name" to (currentList.name),
        "Date Created" to (currentList.dateCreated.toDateString()),
    )
    val bodyItems: Map<String, String> = mapOf(
        "Totel Items" to itemCount.toString(),
        "Assigned Items" to assignedItemCount.toString(),
        "Completed Items" to completedItemCount.toString(),
        "With Due Date" to dueItemCount.toString(),
        "With Budget" to budgetedItemCount.toString(),
        "Total Budget" to totalBudget.toCurrency()
    )

    fun setUpForRename() {
        state.value = state.value.copy(showRename = true, newName = currentList.name)
    }

    fun doRename() {
        if (state.value.newName != currentList.name) {
            doRename(state.value.newName)
        }
        state.value = state.value.copy(showRename = false)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth(),
                title = {
                    if (state.value.showRename) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AutofocusTextInput(
                                value = state.value.newName,
                                onValueChange = {
                                    state.value =
                                        state.value.copy(newName = it.filterText("\r\n"))
                                },
                                label = { Text(text = "New Name") },
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    focusedLabelColor = MaterialTheme.colorScheme.onSecondary,
                                    cursorColor = MaterialTheme.colorScheme.onSecondary
                                ),
                                modifier = Modifier.weight(.8f)
                            )
                            Spacer(modifier = Modifier.size(8.dp))
                            IconButton(
                                onClick = { doRename() },
                                enabled = state.value.newName.isNotEmpty()
                            ) {
                                Icon(Icons.Default.Check, "Rename List")
                            }
                            Spacer(modifier = Modifier.size(8.dp))
                            IconButton(
                                onClick = {
                                    state.value = state.value.copy(
                                        showRename = false,
                                        newName = currentList.name
                                    )
                                }
                            ) {
                                Icon(Icons.Default.Close, "Cancel")
                            }
                        }
                    }
                    if (!state.value.showRename) {
                        Text(
                            text = currentList.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            fontFamily = lora
                        )
                    }
                },
                actions = {
                    if (!state.value.showRename) {
                        IconButton(
                            onClick = {
                                navController.navigate("root")
                            }
                        ) {
                            Icon(Icons.Default.Home, "Go Home")
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
        },
        floatingActionButton = {
            StandardFAB(
                text = "Rename",
                icon = Icons.Default.Edit,
                click = { setUpForRename() },
                contentDescription = "Rename List"
            )
        },
        isFloatingActionButtonDocked = true,
        floatingActionButtonPosition = FabPosition.End,
        bottomBar = {
            StandardBottomBar(text = "About this List")
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
            Column(
                modifier = Modifier
                    .padding(it)
                    .padding(start = 8.dp, end = 8.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    elevation = 24.dp
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        for (headerItem in headerItems) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = headerItem.key,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(.5f)
                                )
                                Text(
                                    text = headerItem.value,
                                    fontSize = 24.sp,
                                    fontStyle = FontStyle.Italic,
                                    textAlign = TextAlign.End,
                                    modifier = Modifier.weight(.5f)
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                for (bodyItem in bodyItems) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = bodyItem.key,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(.5f)
                        )
                        Text(
                            text = bodyItem.value,
                            fontSize = 16.sp,
                            fontStyle = FontStyle.Italic,
                            textAlign = TextAlign.End,
                            modifier = Modifier.weight(.5f)
                        )
                        Spacer(modifier = Modifier.size(8.dp))
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                if (categoryCount == 0) {
                    Text(
                        text = "No Categories Defined",
                        fontStyle = FontStyle.Italic
                    )
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(all = 8.dp)
                            .border(BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)),
                        elevation = 20.dp
                    ) {
                        Expander(
                            title = "Categories (${categories.size})",
                            items = categories.map { category -> category.name },
                            itemLeadSpace = 36.dp
                        )
                    }
                }
            }
        }
    )
}