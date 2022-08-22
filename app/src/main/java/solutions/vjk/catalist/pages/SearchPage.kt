package solutions.vjk.catalist.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import solutions.vjk.catalist.infrastructure.filterText
import solutions.vjk.catalist.models.Item
import solutions.vjk.catalist.state.SearchState
import solutions.vjk.catalist.widgets.AutofocusTextInput
import solutions.vjk.catalist.widgets.NavMenu
import solutions.vjk.catalist.widgets.StandardBottomBar

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchPage(
    items: List<Item>,
    navController: NavController
) {
    val scaffoldState =
        rememberScaffoldState(rememberDrawerState(initialValue = DrawerValue.Closed))
    val scope = rememberCoroutineScope()
    val searchState = mutableStateOf(
        SearchState(
            searchText = "",
            matchingItems = ArrayList(items)
        )
    )
    val keyboardController = LocalSoftwareKeyboardController.current

    fun filterItems() {
        val matches = items.filter {
            it.name.contains(searchState.value.searchText, ignoreCase = true)
        }
        searchState.value = searchState.value.copy(matchingItems = matches.toList())
    }

    fun doEdit(id: Int) {
        keyboardController?.hide()
        navController.navigate("edit/$id")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colorScheme.secondary,
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AutofocusTextInput(
                            value = searchState.value.searchText,
                            onValueChange = {
                                searchState.value =
                                    searchState.value.copy(searchText = it.filterText("\r\n"))
                                filterItems()
                            },
                            label = {
                                Text(text = "Search Text")
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.textFieldColors(
                                focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                cursorColor = MaterialTheme.colorScheme.onPrimary
                            )
                        )
                        Spacer(modifier = Modifier.size(16.dp))
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate("root")
                        }
                    ) {
                        Icon(Icons.Default.Home, "Go Home")
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
        bottomBar = {
            StandardBottomBar(text = "Search Items")
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
            LazyColumn(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                items(searchState.value.matchingItems) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .clickable {
                                doEdit(item.id)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.name,
                            fontSize = 24.sp
                        )
                    }
                    Divider(
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    )
}