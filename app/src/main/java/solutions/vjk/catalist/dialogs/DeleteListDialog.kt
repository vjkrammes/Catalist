package solutions.vjk.catalist.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import solutions.vjk.catalist.models.ToDoList

@Composable
fun DeleteListDialog(
    list: ToDoList,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    onOK: () -> Unit,
    onCancel: () -> Unit,
) {
    val warningText =
        "You are about to delete the list named '${list.name}'. All data associated with this list " +
                "will be deleted. This includes all items and categories " +
                "associated with this list. This action cannot be undone."
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                setShowDialog(false)
                onCancel()
            },
            modifier = Modifier.fillMaxWidth(),
            buttons = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = {
                            setShowDialog(false)
                            onOK()
                        },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
                    ) {
                        Icon(Icons.Default.Check, "OK")
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("OK")
                    }
                    TextButton(
                        onClick = {
                            setShowDialog(false)
                            onCancel()
                        },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
                    ) {
                        Icon(Icons.Default.Clear, "Cancel")
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("Cancel")
                    }
                }
            },
            title = {
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .fillMaxWidth(),
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Delete a List",
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                }
            },
            text = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                    elevation = 12.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = warningText,
                            modifier = Modifier.fillMaxWidth(),
                            fontStyle = FontStyle.Italic,
                        )
                    }
                }
            }
        )
    }
}