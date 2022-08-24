package solutions.vjk.catalist.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import solutions.vjk.catalist.models.Assignee
import solutions.vjk.catalist.ui.theme.gray1

@Composable
fun SelectAssigneeWidget(
    displayedAssigneeName: String,
    doSetAssignee: (Assignee) -> Unit,
    assignees: List<Assignee>
) {

    val assigneeListExpanded = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FormLabel(
            text = "Assignee",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.TopStart)
        ) {
            Text(
                text = displayedAssigneeName,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(assignees.isNotEmpty()) { assigneeListExpanded.value = true }
                    .background(gray1)
                    .padding(vertical = 8.dp, horizontal = 4.dp)
            )
            DropdownMenu(
                expanded = assigneeListExpanded.value,
                onDismissRequest = {
                    assigneeListExpanded.value = false
                }
            ) {
                DropdownMenuItem(
                    onClick =
                    {
                        doSetAssignee(Assignee(name = "Me"))
                        assigneeListExpanded.value = false
                    }
                ) {
                    Text(text = "Me")
                }
                assignees.forEach { assignee ->
                    DropdownMenuItem(
                        onClick = {
                            doSetAssignee(assignee)
                            assigneeListExpanded.value = false
                        }
                    ) {
                        Text(text = assignee.name)
                    }
                }
            }
        }
    }
}
