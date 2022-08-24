package solutions.vjk.catalist.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.SharedFlow
import solutions.vjk.catalist.models.ToDoList
import solutions.vjk.catalist.state.NewListState
import solutions.vjk.catalist.ui.theme.*
import solutions.vjk.catalist.widgets.*

@Composable
fun NewListPage(
    state: NewListState,
    navController: NavController,
    doSetName: (String) -> Unit,
    doToggleImport: () -> Unit,
    doSelectList: (ToDoList) -> Unit,
    doSave: (NavController) -> Unit,
    toastMessage: SharedFlow<String>
) {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val isListExpanded = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        toastMessage
            .collect { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        if (!state.isLoading) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState,
                topBar = {
                    StandardTopBar(
                        text = "Create a New List",
                        scaffoldState = scaffoldState,
                        scope = scope,
                        navController = navController
                    )
                },
                bottomBar = {
                    StandardBottomBar(text = "Create List")
                },
                floatingActionButton = {
                    StandardFAB(
                        text = "Create",
                        icon = Icons.Default.Build,
                        click = { doSave(navController) },
                        contentDescription = "Create List"
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
                    SnackbarHost(hostState = it)
                },
                content = {
                    Surface(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxWidth()
                            .padding(all = 12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AutofocusTextInput(
                                value = state.name,
                                onValueChange = doSetName,
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text(text = "Name") },
                                placeholder = { Text(text = "List Name") },
                                singleLine = true,
                                colors = TextFieldDefaults.textFieldColors(
                                    textColor = MaterialTheme.colorScheme.onSurface,
                                    cursorColor = MaterialTheme.colorScheme.onSurface,
                                )
                            )
                            Divider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                Switch(
                                    checked = state.importCategories,
                                    onCheckedChange = { doToggleImport() },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = green3,
                                        uncheckedThumbColor = gray4,
                                        checkedTrackColor = green2,
                                        uncheckedTrackColor = gray3,
                                    )
                                )
                                Text(text = "Import Categories")
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 60.dp)
                                        .fillMaxWidth()
                                        .wrapContentSize(Alignment.TopStart)
                                ) {
                                    Text(
                                        text = state.displayedListName,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(
                                                state.lists.isNotEmpty() && state.importCategories
                                            ) {
                                                isListExpanded.value = !isListExpanded.value
                                            }
                                            .background(gray1)
                                            .padding(vertical = 4.dp),
                                    )
                                    DropdownMenu(
                                        expanded = isListExpanded.value,
                                        onDismissRequest =
                                        {
                                            isListExpanded.value = false
                                        }
                                    ) {
                                        state.lists.forEach { list ->
                                            DropdownMenuItem(
                                                onClick = {
                                                    doSelectList(list)
                                                    isListExpanded.value = false
                                                }
                                            ) {
                                                Text(text = list.name)
                                            }
                                        }
                                    }
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