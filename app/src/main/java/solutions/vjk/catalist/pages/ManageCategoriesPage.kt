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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.SharedFlow
import solutions.vjk.catalist.dialogs.RenameCategoryDialog
import solutions.vjk.catalist.infrastructure.filterText
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.state.ManageCategoriesState
import solutions.vjk.catalist.state.RenameCategoryState
import solutions.vjk.catalist.widgets.*

@Composable
fun ManageCategoriesPage(
    state: ManageCategoriesState,
    navController: NavController,
    doNew: (String) -> Unit,
    doDelete: (Category) -> Unit,
    doDetails: (Category, NavController) -> Unit,
    toastMessage: SharedFlow<String>
) {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val (showInput, setShowInput) = remember { mutableStateOf(false) }
    val (showRenameDialog, setShowRenameDialog) = remember { mutableStateOf(false) }

    val renameState = remember {
        mutableStateOf(
            RenameCategoryState(
                category = Category(),
                newName = ""
            )
        )
    }

    LaunchedEffect(Unit) {
        toastMessage
            .collect { message ->
                scaffoldState.snackbarHostState.showSnackbar(message)
            }
    }

    fun setUpForNewCategory() {
        setShowInput(!showInput)
    }

    fun rename(category: Category) {
        renameState.value = renameState.value.copy(
            category = category,
            newName = category.name
        )
        setShowRenameDialog(true)
    }

    fun setName(name: String) {
        renameState.value = renameState.value.copy(
            newName = name.filterText("\r\n")
        )
    }

    fun cancel() {
        setShowRenameDialog(false)
        renameState.value = renameState.value.copy(
            category = Category(),
            newName = ""
        )
    }

    fun ok() {

    }

    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        if (!state.isLoading) {
            Scaffold(
                modifier = Modifier.fillMaxWidth(),
                scaffoldState = scaffoldState,
                topBar = {
                    CategoryTopAppBar(
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
                        setUpForNewCategory()
                    }
                },
                isFloatingActionButtonDocked = true,
                floatingActionButtonPosition = FabPosition.End,
                bottomBar = {
                    StandardBottomBar(text = "Manage Categories")
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
                    if (state.categories.isEmpty()) {
                        Text(
                            text = "No Categories Found",
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                                .padding(top = 40.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .padding(it)
                                .fillMaxWidth()
                        ) {
                            items(state.categories) { category ->
                                CategoryCard(
                                    category = category,
                                    itemCount =
                                    state.items.singleOrNull { ic -> ic.id == category.id }?.count
                                        ?: 0,
                                    onButtonClick = doDelete,
                                    onCategoryClick = { c ->
                                        doDetails(c, navController)
                                    },
                                    onLongClick = { rename(category) }
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
        RenameCategoryDialog(
            state = renameState.value,
            showDialog = showRenameDialog,
            setShowDialog = setShowRenameDialog,
            setName = { name -> setName(name) },
            doOK = { ok() },
            doCancel = { cancel() }
        )
    }
}