package solutions.vjk.catalist.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import solutions.vjk.catalist.ui.theme.gray3
import solutions.vjk.catalist.ui.theme.gray4
import solutions.vjk.catalist.ui.theme.green2
import solutions.vjk.catalist.ui.theme.green3

@Composable
fun ItemToggleWidget(
    isInProgress: Boolean,
    isComplete: Boolean,
    toggleInProgress: () -> Unit,
    toggleComplete: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FormLabel(
                text = "Toggles",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                FormLabel(
                    text = "In Progress",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
                Switch(
                    modifier = Modifier.weight(1f),
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = green3,
                        uncheckedThumbColor = gray4,
                        checkedTrackColor = green2,
                        uncheckedTrackColor = gray3
                    ),
                    checked = isInProgress,
                    onCheckedChange = { toggleInProgress() }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                FormLabel(
                    text = "Complete",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
                Switch(
                    modifier = Modifier.weight(1f),
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = green3,
                        uncheckedThumbColor = gray4,
                        checkedTrackColor = green2,
                        uncheckedTrackColor = gray3
                    ),
                    checked = isComplete,
                    onCheckedChange = { toggleComplete() }
                )
            }
        }
    }
}