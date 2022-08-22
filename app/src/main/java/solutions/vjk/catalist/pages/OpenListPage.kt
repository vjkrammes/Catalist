package solutions.vjk.catalist.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.SharedFlow
import solutions.vjk.catalist.dialogs.DeleteListDialog
import solutions.vjk.catalist.models.ToDoList
import solutions.vjk.catalist.state.OpenListState
import solutions.vjk.catalist.widgets.ListBadge
import solutions.vjk.catalist.widgets.NavMenu
import solutions.vjk.catalist.widgets.StandardBottomBar
import solutions.vjk.catalist.widgets.StandardTopBar

@Composable
fun OpenListPage(
    state: OpenListState,
    doDeleteList: (Int) -> Unit,
    doSwitchToList: (String, NavController) -> Unit,
    navController: NavController,
    toastMessage: SharedFlow<String>
) {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    val selectedList = remember { mutableStateOf(ToDoList()) }

    LaunchedEffect(Unit) {
        toastMessage
            .collect { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
    }

    fun confirm(list: ToDoList) {
        selectedList.value = list
        setShowDialog(true)
    }

    fun ok() {
        setShowDialog(false)
        if (selectedList.value.id != 0) {
            doDeleteList(selectedList.value.id)
        }
    }

    fun cancel() {
        setShowDialog(false)
    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!state.isLoading) {
            Scaffold(
                modifier = Modifier
                    .padding(all = 8.dp)
                    .fillMaxWidth(),
                scaffoldState = scaffoldState,
                topBar = {
                    StandardTopBar(
                        text = "Open a List",
                        scaffoldState = scaffoldState,
                        scope = scope,
                        navController = navController
                    )
                },
                bottomBar = {
                    StandardBottomBar(text = "Open List")
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
                            .fillMaxWidth()
                            .padding(it)
                    ) {
                        LazyColumn {
                            items(state.lists) { list ->
                                ListBadge(
                                    list = list,
                                    doDelete = { confirm(list) },
                                    deleteEnabled = list.name != state.currentListName,
                                    doSwitchTo = { doSwitchToList(list.name, navController) }
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
        DeleteListDialog(
            list = selectedList.value,
            showDialog = showDialog,
            setShowDialog = setShowDialog,
            onOK = { ok() },
            onCancel = { cancel() },
        )
    }
}