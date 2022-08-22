package solutions.vjk.catalist.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import solutions.vjk.catalist.R
import solutions.vjk.catalist.models.Assignee

@Composable
fun AssigneeCard(
    assignee: Assignee,
    assignmentCount: Int,
    onButtonClick: (Assignee) -> Unit,
    onAssigneeClick: (Assignee) -> Unit,
    rowHeight: Dp = 40.dp
) {
    Row(
        modifier = Modifier
            .padding(all = 8.dp)
            .fillMaxWidth()
            .height(rowHeight)
            .clickable { onAssigneeClick(assignee) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { onButtonClick(assignee) },
            enabled = assignmentCount == 0
        ) {
            Icon(Icons.Default.Delete, "Delete this Assignee")
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = assignee.name,
            fontSize = 24.sp,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis
        )
        if (assignmentCount > 0) {
            Text(
                text = assignmentCount.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                fontStyle = FontStyle.Italic
            )
        }
    }
}