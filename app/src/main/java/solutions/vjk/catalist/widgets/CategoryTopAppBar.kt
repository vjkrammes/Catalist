package solutions.vjk.catalist.widgets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import solutions.vjk.catalist.infrastructure.filterText

@Composable
fun CategoryTopAppBar(
    showInput: Boolean,
    setShowInput: (Boolean) -> Unit,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    navController: NavController,
    doNew: (String) -> Unit
) {
    val name = remember { mutableStateOf("") }

    fun addCategory() {
        setShowInput(false)
        doNew(name.value)
        name.value = ""
    }

    TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.secondary,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showInput) {
                    AutofocusTextInput(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        value = name.value,
                        onValueChange = { name.value = it.filterText("\r\n") },
                        label = { Text("Name") },
                        placeholder = { Text("Category Name") },
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            focusedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            cursorColor = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                    Spacer(modifier = Modifier.size(16.dp))
                    IconButton(
                        onClick = { addCategory() },
                        enabled = name.value.isNotEmpty()
                    ) {
                        Icon(Icons.Default.Check, "Add Category")
                    }
                }
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
}