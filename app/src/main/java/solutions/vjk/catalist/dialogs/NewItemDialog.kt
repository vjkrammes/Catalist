package solutions.vjk.catalist.dialogs

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import solutions.vjk.catalist.infrastructure.filterText
import solutions.vjk.catalist.models.Category
import solutions.vjk.catalist.ui.theme.gray1
import solutions.vjk.catalist.widgets.AutofocusTextInput

@Composable
fun NewItemDialog(
    categories: List<Category>,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    onOK: (String, Category) -> Unit,
    onCancel: () -> Unit,
    onAdvanced: () -> Unit,
    doSetCategory: (Category) -> Unit,
    selectedCategory: Category
) {
    val name = remember { mutableStateOf("") }
    val categoryListExpanded = remember { mutableStateOf(false) }
    val displayedCategoryName = remember { mutableStateOf("") }

    if (displayedCategoryName.value != selectedCategory.name) {
        val c = categories.singleOrNull { it.name == selectedCategory.name }
        if (c == null) {
            displayedCategoryName.value = categories[0].name
        } else {
            displayedCategoryName.value = c.name
        }
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                setShowDialog(false)
                name.value = ""
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
                            onOK(name.value, selectedCategory)
                            name.value = ""
                        },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface),
                        enabled = name.value.isNotEmpty()
                    ) {
                        Icon(Icons.Default.Check, "OK")
                        Spacer(modifier = Modifier.width(16.dp))
                        Text("OK")
                    }
                    TextButton(
                        onClick = {
                            setShowDialog(false)
                            name.value = ""
                            onCancel()
                        },
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.onSurface)
                    ) {
                        Icon(Icons.Default.Clear, "Cancel")
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = "Cancel")
                    }
                }
            },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Add an Item",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    IconButton(
                        onClick = {
                            setShowDialog(false)
                            name.value = ""
                            onAdvanced()
                        }
                    ) {
                        Icon(Icons.Default.MoreVert, "Advanced create")
                    }
                }
            },
            text = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(all = 8.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Category",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontStyle = FontStyle.Italic
                        )
                        Text(
                            text = displayedCategoryName.value,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(gray1)
                                .clickable { categoryListExpanded.value = true }
                                .padding(vertical = 8.dp, horizontal = 4.dp)
                        )
                        DropdownMenu(
                            expanded = categoryListExpanded.value,
                            onDismissRequest =
                            {
                                categoryListExpanded.value = false
                            }
                        ) {
                            categories.forEach { category ->
                                DropdownMenuItem(
                                    onClick = {
                                        displayedCategoryName.value = category.name
                                        doSetCategory(category)
                                        categoryListExpanded.value = false
                                    }
                                ) {
                                    Text(text = category.name)
                                }
                            }
                        }
                        Text(
                            text = "Description",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontStyle = FontStyle.Italic
                        )
                        AutofocusTextInput(
                            value = name.value,
                            onValueChange = {
                                name.value = it.filterText("\r\n")
                            },
                            singleLine = true,
                            label = { Text("Name") },
                            placeholder = { Text("Item Name") },
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colorScheme.onSurface,
                                cursorColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
        )
    }
}