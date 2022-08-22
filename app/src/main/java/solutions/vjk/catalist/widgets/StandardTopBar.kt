package solutions.vjk.catalist.widgets

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import solutions.vjk.catalist.R

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StandardTopBar(
    text: String,
    scaffoldState: ScaffoldState,
    scope: CoroutineScope,
    navController: NavController,
    fontSize: TextUnit = 24.sp,
    additionalButton: @Composable (() -> Unit)? = null,
    additionalButtons: List<@Composable (() -> Unit)>? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val lara = FontFamily(Font(R.font.lora, FontWeight.Bold))
    TopAppBar(
        backgroundColor = MaterialTheme.colorScheme.secondary,
        title = {
            Text(
                text = text,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                fontFamily = lara
            )
        },
        actions = {
            if (additionalButtons != null) {
                for (button in additionalButtons) {
                    keyboardController?.hide()
                    button()
                }
            }
            if (additionalButton != null) {
                keyboardController?.hide()
                additionalButton()
            }
            IconButton(
                onClick = {
                    keyboardController?.hide()
                    navController.navigate("root")
                }
            ) {
                Icon(Icons.Default.Home, "Go Home")
            }
            IconButton(
                onClick = {
                    keyboardController?.hide()
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