package solutions.vjk.catalist.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import solutions.vjk.catalist.state.RenameCategoryState
import solutions.vjk.catalist.widgets.AutofocusTextInput

@Composable
fun RenameCategoryDialog(
    state: RenameCategoryState,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    setName: (String) -> Unit,
    doOK: () -> Unit,
    doCancel: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                setShowDialog(false)
                setName("")
            },
            modifier = Modifier.fillMaxWidth(0.9f),
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = {
                            setShowDialog(false)
                            doOK()
                        },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
                        enabled = state.newName.isNotEmpty()
                    ) {
                        Icon(Icons.Default.Check, "OK")
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "OK")
                    }
                    TextButton(
                        onClick = {
                            setShowDialog(false)
                            doCancel()
                        },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
                    ) {
                        Icon(Icons.Default.Close, "Cancel")
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Cancel")
                    }
                }
            },
            title = {
                Text(
                    text = "Rename Category",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)
                ) {
                    AutofocusTextInput(
                        value = state.newName,
                        onValueChange = setName,
                        singleLine = true,
                        placeholder = { Text(text = "New Name") },
                        label = { Text("Name") },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = MaterialTheme.colorScheme.onSurface,
                            cursorColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }
        )
    }
}