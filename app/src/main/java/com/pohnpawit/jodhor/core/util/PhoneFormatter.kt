package com.pohnpawit.jodhor.core.util

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

fun formatThaiPhone(input: String): String {
    val d = input.filter(Char::isDigit)
    if (d.startsWith("02") && d.length in 1..9) {
        return when (d.length) {
            1, 2 -> d
            in 3..5 -> "${d.take(2)}-${d.drop(2)}"
            in 6..9 -> "${d.take(2)}-${d.substring(2, 5)}-${d.drop(5)}"
            else -> d
        }
    }
    return when (d.length) {
        in 0..3 -> d
        in 4..6 -> "${d.take(3)}-${d.drop(3)}"
        in 7..10 -> "${d.take(3)}-${d.substring(3, 6)}-${d.drop(6)}"
        else -> d
    }
}

class PhoneVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = formatThaiPhone(text.text)
        return TransformedText(AnnotatedString(formatted), PhoneOffsetMapping(formatted))
    }
}

private class PhoneOffsetMapping(private val formatted: String) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int {
        if (offset == 0) return 0
        var digits = 0
        for (i in formatted.indices) {
            if (formatted[i].isDigit()) {
                digits++
                if (digits == offset) {
                    var j = i + 1
                    while (j < formatted.length && formatted[j] == '-') j++
                    return j
                }
            }
        }
        return formatted.length
    }

    override fun transformedToOriginal(offset: Int): Int {
        var count = 0
        for (i in 0 until minOf(offset, formatted.length)) {
            if (formatted[i].isDigit()) count++
        }
        return count
    }
}
