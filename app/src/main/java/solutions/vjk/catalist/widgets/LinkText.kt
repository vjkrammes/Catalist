package solutions.vjk.catalist.widgets

import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import solutions.vjk.catalist.models.LinkTextItem
import solutions.vjk.catalist.ui.theme.blue3

//
// based on this link: https://stackoverflow.com/questions/65567412/jetpack-compose-text-hyperlink-some-section-of-the-text
//

@Composable
fun LinkText(
    linkTextItems: List<LinkTextItem>,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    fontStyle: FontStyle = FontStyle.Normal
) {
    val annotatedString = createAnnotatedString(linkTextItems)
    ClickableText(
        text = annotatedString,
        style = TextStyle(
            textAlign = textAlign,
            fontStyle = fontStyle
        ),
        onClick = { offset ->
            linkTextItems.forEach { annotatedStringData ->
                if (annotatedStringData.tag != null && annotatedStringData.annotation != null) {
                    annotatedString.getStringAnnotations(
                        tag = annotatedStringData.tag,
                        start = offset,
                        end = offset,
                    ).firstOrNull()?.let {
                        annotatedStringData.onClick?.invoke(it)
                    }
                }
            }
        },
        modifier = modifier
    )
}

@Composable
fun LinkText(
    item: LinkTextItem,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    fontStyle: FontStyle = FontStyle.Normal
) {
    val annotatedString = createAnnotatedString(item)
    ClickableText(
        text = annotatedString,
        style = TextStyle(
            textAlign = textAlign,
            fontStyle = fontStyle
        ),
        onClick = { offset ->
            if (item.tag != null && item.annotation != null) {
                annotatedString.getStringAnnotations(
                    tag = item.tag,
                    start = offset,
                    end = offset
                ).firstOrNull()?.let {
                    item.onClick?.invoke(it)
                }
            }
        },
        modifier = modifier
    )
}

@Composable
private fun createAnnotatedString(data: List<LinkTextItem>): AnnotatedString {
    return buildAnnotatedString {
        data.forEach { linkTextItem ->
            if (linkTextItem.tag != null && linkTextItem.annotation != null) {
                pushStringAnnotation(
                    tag = linkTextItem.tag,
                    annotation = linkTextItem.annotation
                )
                withStyle(
                    style = SpanStyle(
                        color = blue3,
                        textDecoration = TextDecoration.Underline
                    ),
                ) {
                    append(linkTextItem.text)
                }
                pop()
            } else {
                append(linkTextItem.text)
            }
        }
    }
}

@Composable
private fun createAnnotatedString(item: LinkTextItem): AnnotatedString {
    return buildAnnotatedString {
        if (item.tag != null && item.annotation != null) {
            pushStringAnnotation(
                tag = item.tag,
                annotation = item.annotation
            )
            withStyle(
                style = SpanStyle(
                    color = blue3,
                    textDecoration = TextDecoration.Underline
                )
            ) {
                append(item.text)
            }
            pop()
        } else {
            append(item.text)
        }
    }
}