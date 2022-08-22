package solutions.vjk.catalist.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import solutions.vjk.catalist.R
import solutions.vjk.catalist.ui.theme.gray1
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// based on https://www.youtube.com/watch?v=nLwo9BUsqO0

@Composable
fun DateWidget(
    value: LocalDate,
    onValueChanged: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    borderColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled),
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    iconColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = TextFieldDefaults.IconOpacity),
    enabled: Boolean = true,
    errorMessage: String? = null
) {
    val dialogState = rememberMaterialDialogState()

    MaterialDialog(
        dialogState = dialogState,
        buttons = {
            positiveButton(stringResource(id = R.string.ok))
            negativeButton(stringResource(id = R.string.cancel))
        },
        backgroundColor = MaterialTheme.colorScheme.surface,
        autoDismiss = true,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        this.datepicker(
            initialDate = value,
            title = "Select Due Date",
            onDateChange = onValueChanged
        )
    }

    val hasError = errorMessage != null
    val borderColorToUse = if (hasError) MaterialTheme.colorScheme.error else borderColor
    val iconColorToUse = if (hasError) MaterialTheme.colorScheme.error else iconColor

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = borderColorToUse,
                    shape = RoundedCornerShape(50)
                )
                .clip(RoundedCornerShape(50))
                .clickable {
                    if (enabled) {
                        dialogState.show()
                    }
                }
                .background(
                    if (enabled) MaterialTheme.colorScheme.surface else gray1
                )
        ) {
            DateAndIcon(
                value = value,
                textColor = textColor,
                iconColorToUse = iconColorToUse
            )
        }

        if (errorMessage != null) {
            ErrorMessage(errorMessage)
        }
    }
}

@Composable
private fun DateAndIcon(
    value: LocalDate,
    textColor: Color,
    iconColorToUse: Color
) {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = value.toUIString(),
            color = textColor,
            modifier = Modifier.weight(1f)
        )
        Icon(
            Icons.Default.DateRange,
            contentDescription = stringResource(id = R.string.select_date),
            tint = iconColorToUse
        )
    }
}

@Composable
private fun ErrorMessage(errorMessage: String) {
    Text(
        text = errorMessage,
        color = MaterialTheme.colorScheme.error,
        modifier = Modifier
            .padding(
                top = 4.dp,
                start = 16.dp
            )
    )
}

private fun LocalDate.toUIString(): String {
    val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy")
    return formatter.format(this)
}