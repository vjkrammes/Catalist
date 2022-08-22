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
import androidx.compose.ui.unit.dp
import solutions.vjk.catalist.infrastructure.DEFAULT_DATE
import solutions.vjk.catalist.infrastructure.toCalendar
import solutions.vjk.catalist.infrastructure.toLocalDate
import solutions.vjk.catalist.ui.theme.gray3
import solutions.vjk.catalist.ui.theme.gray4
import solutions.vjk.catalist.ui.theme.green2
import solutions.vjk.catalist.ui.theme.green3
import java.time.LocalDate
import java.util.*

@Composable
fun DueDateWidget(
    hasDueDate: Boolean,
    dueDate: Calendar,
    doSetDueDate: (Boolean, Calendar) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            FormLabel(text = "Due Date")
            Switch(
                colors = SwitchDefaults.colors(
                    checkedThumbColor = green3,
                    uncheckedThumbColor = gray4,
                    checkedTrackColor = green2,
                    uncheckedTrackColor = gray3
                ),
                checked = hasDueDate,
                onCheckedChange = {
                    val hdd = !hasDueDate
                    val cal = if (hdd) Calendar.getInstance() else DEFAULT_DATE
                    doSetDueDate(hdd, cal)
                }
            )
        }
        DateWidget(
            value = if (hasDueDate) {
                dueDate.toLocalDate()
            } else {
                LocalDate.now()
            },
            onValueChanged = {
                val cal = it.toCalendar()
                doSetDueDate(true, cal)
            },
            enabled = hasDueDate
        )
    }
}
