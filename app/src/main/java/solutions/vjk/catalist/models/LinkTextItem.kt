package solutions.vjk.catalist.models

import androidx.compose.ui.text.AnnotatedString

//
// code based on this link: https://stackoverflow.com/questions/65567412/jetpack-compose-text-hyperlink-some-section-of-the-text
//

data class LinkTextItem(
    val text: String,
    val tag: String? = null,
    val annotation: String? = null,
    val onClick: ((str: AnnotatedString.Range<String>) -> Unit)? = null
)