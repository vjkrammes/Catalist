package solutions.vjk.catalist.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import solutions.vjk.catalist.models.ToDoList
import solutions.vjk.catalist.R

@Composable
fun ListBadge(
    list: ToDoList,
    doDelete: (Int) -> Unit,
    deleteEnabled: Boolean,
    doSwitchTo: (Int) -> Unit,
    spaceBeetween: Dp = 24.dp,
) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .clickable { doSwitchTo(list.id) },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spaceBeetween)
    ) {
        IconButton(
            onClick = { doDelete(list.id) },
            enabled = deleteEnabled,
        ) {
            Icon(Icons.Default.Delete, "Delete this list")
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            text = list.name,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}