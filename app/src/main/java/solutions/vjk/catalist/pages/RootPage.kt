package solutions.vjk.catalist.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.flow.SharedFlow
import solutions.vjk.catalist.R
import solutions.vjk.catalist.dialogs.NewItemDialog
import solutions.vjk.catalist.dialogs.RenameCategoryDialog
import solutions.vjk.catalist.infrastructure.filterText
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.models.Item
import solutions.vjk.catalist.state.ItemState
import solutions.vjk.catalist.state.RenameCategoryState
import solutions.vjk.catalist.widgets.*

@Composable
fun RootPage(
    state: ItemState,
    navController: NavController,
    doDelete: (Item) -> Unit,
    doComplete: (Item) -> Unit,
    doEdit: (Item) -> Unit,
    doRenameCategory: (Category, String) -> Unit,
    addBasicItem: (String, Category) -> Unit,
    addAdvancedItem: () -> Unit,
    noCategories: () -> Unit,
    toastMessage: SharedFlow<String>
) {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val logoBitmap = ImageBitmap.imageResource(id = R.drawable.logo64)
    val (showNewItemDialog, setShowNewItemDialog) = remember { mutableStateOf(false) }
    val lora = FontFamily(Font(R.font.lora, weight = FontWeight.Normal))
    val selectedCategory = remember { mutableStateOf(Category()) }

    val renameState = remember {
        mutableStateOf(
            RenameCategoryState(
                category = Category(),
                newName = ""
            )
        )
    }
    val (showRenameDialog, setShowRenameDialog) = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        toastMessage
            .collect { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            }
    }

    fun doLongClick(category: Category) {
        renameState.value = renameState.value.copy(
            category = category,
            newName = category.name
        )
        setShowRenameDialog(true)
    }

    fun setNewName(value: String) {
        renameState.value = renameState.value.copy(newName = value.filterText("\r\n"))
    }

    fun cancelRename() {
        setShowRenameDialog(false)
        renameState.value = renameState.value.copy(
            category = Category(),
            newName = ""
        )
    }

    fun doRename() {
        doRenameCategory(renameState.value.category, renameState.value.newName)
    }

    fun showOverdue() {
        navController.navigate("dueitems")
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        if (!state.isLoading) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                scaffoldState = scaffoldState,
                topBar = {
                    RootTopAppBar(
                        title = {
                            Text(
                                text = state.currentList.name,
                                fontWeight = FontWeight.Bold,
                                fontFamily = lora,
                                fontSize = 24.sp
                            )
                        },
                        scaffoldState = scaffoldState,
                        scope = scope,
                        dueCount = state.dueCount,
                        overdueCount = state.overdueCount,
                        showOverdue = { showOverdue() }
                    )
                },
                floatingActionButton = {
                    RootFAB(
                        click = {
                            if (state.categories.isEmpty()) {
                                noCategories()
                            } else {
                                setShowNewItemDialog(true)
                            }
                        }
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
                bottomBar = {
                    RootBottomAppBar(
                        logo = logoBitmap,
                        appName = stringResource(id = R.string.app_name),
                        fontFamily = lora,
                        fontStyle = FontStyle.Italic,
                        fontSize = 24.sp
                    )
                },
                snackbarHost = {

                },
                content = {
                    Surface(
                        modifier = Modifier.padding(it)
                    ) {
                        CategoryWidget(
                            categories = state.categories,
                            items = state.items,
                            onIncompleteClick = doComplete,
                            onCompletedClick = doDelete,
                            onItemClick = doEdit,
                            onItemLongClick = { category -> doLongClick(category) },
                            backgroundColor = MaterialTheme.colorScheme.primary,
                            backgroundAlpha = 0.25f
                        )
                    }
                }
            )
        }
        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.error,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        NewItemDialog(
            categories = state.categories,
            showDialog = showNewItemDialog,
            setShowDialog = setShowNewItemDialog,
            onOK = addBasicItem,
            onCancel = { setShowNewItemDialog(false) },
            onAdvanced = addAdvancedItem,
            doSetCategory = { category ->
                selectedCategory.value = category
            },
            selectedCategory = selectedCategory.value
        )
        RenameCategoryDialog(
            state = renameState.value,
            showDialog = showRenameDialog,
            setShowDialog = setShowRenameDialog,
            setName = { name -> setNewName(name) },
            doOK = { doRename() },
            doCancel = { cancelRename() }
        )
    }
}