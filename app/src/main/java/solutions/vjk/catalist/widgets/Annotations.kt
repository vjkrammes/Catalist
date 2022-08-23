package solutions.vjk.catalist.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import solutions.vjk.catalist.R
import solutions.vjk.catalist.infrastructure.DEFAULT_DATE
import solutions.vjk.catalist.infrastructure.toInt
import solutions.vjk.catalist.models.Item
import solutions.vjk.catalist.ui.theme.yellow4
import solutions.vjk.catalist.ui.theme.yellow5
import java.util.*

@Composable
fun Annotations(
    item: Item,
    height: Dp = 20.dp
) {
    Row(
        modifier = Modifier
            .height(height)
            .fillMaxWidth(1f),
        horizontalArrangement = Arrangement.End
    ) {
        val iconHeight = height * 0.75f
        if (item.assigneeId != 0) {
            Image(
                painterResource(
                    id = R.drawable.ic_baseline_person_24
                ),
                contentDescription = "",
                modifier = Modifier.height(iconHeight)
            )
        }
        if (item.budget != 0) {
            Image(
                painterResource(
                    id = R.drawable.ic_baseline_money_24
                ),
                contentDescription = "",
                modifier = Modifier.height(iconHeight)
            )
        }
        if (item.inProgress) {
            Image(
                painterResource(
                    id = R.drawable.ic_baseline_more_horiz_24
                ),
                contentDescription = "",
                modifier = Modifier.height(iconHeight)
            )
        }
        if (item.complete) {
            Image(
                painterResource(
                    id = R.drawable.ic_baseline_check_24
                ),
                contentDescription = "",
                modifier = Modifier.height(iconHeight)
            )
        }
        if (item.dueDate != DEFAULT_DATE) {
            val today = Calendar.getInstance()
            val insevendays = Calendar.getInstance()
            insevendays.add(Calendar.DAY_OF_YEAR, 7)
            val tint = if (item.dueDate.toInt() < today.toInt()) {
                MaterialTheme.colorScheme.error
            } else {
                if (item.dueDate.toInt() < insevendays.toInt()) {
                    yellow5
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            }
            Image(
                painterResource(
                    id = R.drawable.ic_baseline_calendar_month_24
                ),
                contentDescription = "",
                modifier = Modifier.height(iconHeight),
                colorFilter = ColorFilter.tint(tint)
            )
        }
    }
}