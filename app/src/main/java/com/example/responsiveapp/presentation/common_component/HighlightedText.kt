package com.example.responsiveapp.presentation.common_component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle

@Composable
fun HighLightedText(
    modifier: Modifier = Modifier,
    normal: String? = null,
    highlighted: String? = null,
    normalStyle: SpanStyle? = null,
    highlightedStyle: SpanStyle? = null,
    textStyle: TextStyle,
    textAlign: TextAlign? = null,
    onClick: () -> Unit
) {
    val annotatedString = buildAnnotatedString {
        if (normal != null) {
            withStyle(
                style = normalStyle ?: SpanStyle()
            ) {
                append(normal)
            }
        }
        if (highlighted != null) {
            withStyle(
                style = highlightedStyle ?: SpanStyle()
            ) {
                append(highlighted)
            }
        }
    }

    Text(text = annotatedString, modifier = modifier
        .clickable(
            enabled = true,
            onClick = onClick
        )
    , style = textStyle, textAlign = textAlign)
}
