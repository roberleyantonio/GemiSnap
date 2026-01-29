@file:Suppress("SameReturnValue", "SameReturnValue")

package br.com.dev360.gemisnap.core.shared.extensions

import android.text.Spanned
import androidx.core.text.HtmlCompat

fun String.Companion.empty(): String = ""

fun String.toHtml(): Spanned = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)
