package solutions.vjk.catalist.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import solutions.vjk.catalist.R
import solutions.vjk.catalist.models.Item
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
        if (item.dueDate.get(Calendar.YEAR) > 2020) {
            Image(
                painterResource(
                    id = R.drawable.ic_baseline_watch_later_24
                ),
                contentDescription = "",
                modifier = Modifier.height(iconHeight)
            )
        }
    }
}