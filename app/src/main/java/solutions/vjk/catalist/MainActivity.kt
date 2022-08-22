package solutions.vjk.catalist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import solutions.vjk.catalist.pages.*
import solutions.vjk.catalist.transitions.rootEnterTransition
import solutions.vjk.catalist.transitions.rootExitTransition
import solutions.vjk.catalist.transitions.standardEnterTransition
import solutions.vjk.catalist.transitions.standardExitTransition
import solutions.vjk.catalist.ui.theme.CatalistTheme
import solutions.vjk.catalist.viewmodels.*

private const val ASSIGNEE_ID: String = "assigneeId"
private const val CATEGORY_ID: String = "categoryId"
private const val ITEM_ID: String = "itemId"
private const val LIST_NAME: String = "listName"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val assigneeDetailViewModel: AssigneeDetailViewModel by viewModels()
    private val categoryDetailViewModel: CategoryDetailViewModel by viewModels()
    private val editItemViewModel: EditItemViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val manageAssigneesViewModel: ManageAssigneesViewModel by viewModels()
    private val manageCategoriesViewModel: ManageCategoriesViewModel by viewModels()
    private val newItemViewModel: NewItemViewModel by viewModels()
    private val newListViewModel: NewListViewModel by viewModels()
    private val openListViewModel: OpenListViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatalistTheme {
                val navController = rememberAnimatedNavController()
                AnimatedNavHost(navController = navController, startDestination = "root") {
                    composable(
                        route = "root",
                        exitTransition = {
                            rootExitTransition
                            fadeOut(animationSpec = tween(300))
                        },
                        popEnterTransition = {
                            rootEnterTransition
                            fadeIn(animationSpec = tween(300))
                        }
                    ) {
                        RootPage(
                            state = mainViewModel.state.value,
                            navController = navController,
                            doDelete = mainViewModel::deleteItem,
                            doComplete = mainViewModel::completeItem,
                            doEdit = { item -> navController.navigate("edit/${item.id}") },
                            doRenameCategory = mainViewModel::renameCategory,
                            addBasicItem = mainViewModel::addBasicItem,
                            addAdvancedItem = { navController.navigate("newitem") },
                            noCategories = mainViewModel::noCategories,
                            toastMessage = mainViewModel.toastMessage
                        )
                    }
                    composable(
                        route = "switch/{$LIST_NAME}",
                        arguments = listOf(
                            navArgument(LIST_NAME) {
                                type = NavType.StringType
                            }
                        )
                    ) { scope ->
                        val listName = scope.arguments?.getString(LIST_NAME)
                        if (!listName.isNullOrEmpty()) {
                            mainViewModel.switchToList(listName)
                            navController.navigate("root")
                        }
                    }
                    composable(route = "openlist",
                        enterTransition = {
                            standardEnterTransition
                            fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            standardExitTransition
                            fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        openListViewModel.setCurrentList(mainViewModel.state.value.currentList.name)
                        OpenListPage(
                            state = openListViewModel.state.value,
                            doDeleteList = openListViewModel::deleteList,
                            doSwitchToList = openListViewModel::switchTo,
                            navController = navController,
                            toastMessage = openListViewModel.toastMessage
                        )
                    }
                    composable(route = "newlist",
                        enterTransition = {
                            standardEnterTransition
                            fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            standardExitTransition
                            fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        NewListPage(
                            state = newListViewModel.state.value,
                            navController = navController,
                            doSetName = newListViewModel::setName,
                            doToggleImport = newListViewModel::toggleImport,
                            doSelectList = newListViewModel::selectList,
                            doToggleSwitch = newListViewModel::toggleSwitch,
                            doToggleDefault = newListViewModel::toggleDefault,
                            doSave = newListViewModel::create,
                            toastMessage = newItemViewModel.toastMessage
                        )
                    }
                    composable(route = "assignees",
                        enterTransition = {
                            standardEnterTransition
                            fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            standardExitTransition
                            fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        manageAssigneesViewModel.loadAssignees()
                        ManageAssigneesPage(
                            state = manageAssigneesViewModel.state.value,
                            navController = navController,
                            doNew = manageAssigneesViewModel::addAssignee,
                            doDelete = manageAssigneesViewModel::deleteAssignee,
                            doDetails = manageAssigneesViewModel::assigneeDetails,
                            toastMessage = manageAssigneesViewModel.toastMessage
                        )
                    }
                    composable(
                        route = "assigneedetail/{$ASSIGNEE_ID}",
                        arguments = listOf(
                            navArgument(ASSIGNEE_ID) {
                                type = NavType.StringType
                            }),
                        enterTransition = {
                            standardEnterTransition
                            fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            standardExitTransition
                            fadeOut(animationSpec = tween(300))
                        }
                    ) { scope ->
                        val idstring = scope.arguments?.getString(ASSIGNEE_ID)
                        if (!idstring.isNullOrEmpty()) {
                            val id = idstring.toInt()
                            if (id > 0) {
                                assigneeDetailViewModel.load(id)
                                AssigneeDetailPage(
                                    assigneeDetailViewModel.state.value,
                                    navController,
                                    assigneeDetailViewModel::clearAssignment,
                                    assigneeDetailViewModel::clearAllAssignments,
                                    assigneeDetailViewModel.toastMessage
                                )
                                mainViewModel.refreshItems()
                                manageAssigneesViewModel.loadAssignees()
                            }
                        }
                    }
                    composable(route = "categories",
                        enterTransition = {
                            standardEnterTransition
                            fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            standardExitTransition
                            fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        manageCategoriesViewModel.setListId(mainViewModel.state.value.currentList.id)
                        ManageCategoriesPage(
                            state = manageCategoriesViewModel.state.value,
                            navController = navController,
                            doNew = manageCategoriesViewModel::addCategory,
                            doDelete = manageCategoriesViewModel::deleteCategory,
                            doDetails = manageCategoriesViewModel::categoryDetails,
                            toastMessage = manageCategoriesViewModel.toastMessage
                        )
                        mainViewModel.refreshItems()
                    }
                    composable(
                        route = "categorydetail/{$CATEGORY_ID}",
                        arguments = listOf(
                            navArgument(CATEGORY_ID) {
                                type = NavType.StringType
                            }
                        ),
                        enterTransition = {
                            standardEnterTransition
                            fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            standardExitTransition
                            fadeOut(animationSpec = tween(300))
                        }
                    ) { scope ->
                        val idstring = scope.arguments?.getString(CATEGORY_ID)
                        if (!idstring.isNullOrEmpty()) {
                            val id = idstring.toInt()
                            if (id > 0) {
                                categoryDetailViewModel.load(id)
                                CategoryDetailPage(
                                    state = categoryDetailViewModel.state.value,
                                    navController = navController,
                                    doDeleteItem = categoryDetailViewModel::deleteItem,
                                    doDeleteAllItems = categoryDetailViewModel::deleteAllItems,
                                    toastMessage = categoryDetailViewModel.toastMessage
                                )
                                mainViewModel.refreshItems()
                                manageAssigneesViewModel.loadAssignees()
                            }
                        }
                    }
                    composable(route = "search",
                        enterTransition = {
                            standardEnterTransition
                            fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            standardExitTransition
                            fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        SearchPage(
                            items = mainViewModel.state.value.items,
                            navController = navController
                        )
                    }
                    composable(route = "listinfo",
                        enterTransition = {
                            standardEnterTransition
                            fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            standardExitTransition
                            fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        ListInfoPage(
                            navController = navController,
                            mainViewModel.state.value.currentList,
                            mainViewModel.state.value.items,
                            mainViewModel.state.value.categories,
                            doRename = mainViewModel::renameCurrentList
                        )
                    }
                    composable(route = "settings",
                        enterTransition = {
                            standardEnterTransition
                            fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            standardExitTransition
                            fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        SettingsPage(
                            state = settingsViewModel.state.value,
                            navController = navController,
                            doToggleComplete = settingsViewModel::toggleDeleteOnComplete
                        )
                    }
                    composable(route = "about",
                        enterTransition = {
                            standardEnterTransition
                            fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            standardExitTransition
                            fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        AboutPage(
                            navController = navController,
                            context = this@MainActivity
                        )
                    }
                    composable(
                        route = "newitem",
                        enterTransition = {
                            standardEnterTransition
                            fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            standardExitTransition
                            fadeOut(animationSpec = tween(300))
                        }
                    ) {
                        newItemViewModel.setListId(
                            listId = mainViewModel.state.value.currentList.id
                        )
                        NewItemPage(
                            state = newItemViewModel.state.value,
                            navController = navController,
                            doSetAssignee = newItemViewModel::setAssignee,
                            doSetCategory = newItemViewModel::setCategory,
                            doSetName = newItemViewModel::setName,
                            doSetDueDate = newItemViewModel::setDueDate,
                            doSetBudget = newItemViewModel::setBudget,
                            doToggleInProgress = newItemViewModel::toggleInProgres,
                            doToggleComplete = newItemViewModel::toggleComplete,
                            doSave = newItemViewModel::saveChanges,
                            toastMessage = newItemViewModel.toastMessage
                        )
                        mainViewModel.refreshItems()
                        manageAssigneesViewModel.loadAssignees()
                    }
                    composable(
                        route = "edit/{$ITEM_ID}",
                        arguments = listOf(
                            navArgument(ITEM_ID) {
                                type = NavType.StringType
                            },
                        ),
                        enterTransition = {
                            standardEnterTransition
                            fadeIn(animationSpec = tween(300))
                        },
                        popExitTransition = {
                            standardExitTransition
                            fadeOut(animationSpec = tween(300))
                        }
                    ) { scope ->
                        val idString: String? = scope.arguments?.getString(ITEM_ID)
                        if (!idString.isNullOrEmpty()) {
                            val id = idString.toInt()
                            if (id > 0) {
                                editItemViewModel.setItem(id)
                                EditItemPage(
                                    state = editItemViewModel.state.value,
                                    navController = navController,
                                    doSetAssignee = editItemViewModel::setAssignee,
                                    doSetCategory = editItemViewModel::setCategory,
                                    doSetName = editItemViewModel::setName,
                                    doSetDueDate = editItemViewModel::setDueDate,
                                    doSetBudget = editItemViewModel::setBudget,
                                    doToggleInProgress = editItemViewModel::toggleInProgress,
                                    doToggleComplete = editItemViewModel::toggleComplete,
                                    doSave = editItemViewModel::saveChanges,
                                    doDelete = editItemViewModel::deleteItem,
                                    toastMessage = editItemViewModel.toastMessage
                                )
                                mainViewModel.refreshItems()
                                manageAssigneesViewModel.loadAssignees()
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
        val listName = prefs.getString(CURRENT_LIST_NAME, DEFAULT_LIST_NAME).toString()
        mainViewModel.switchToList(listName)
    }

    override fun onPause() {
        super.onPause()
        if (mainViewModel.isValid()) {
            val prefs = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putString(CURRENT_LIST_NAME, mainViewModel.state.value.currentList.name)
            editor.commit()
        }
    }
}

