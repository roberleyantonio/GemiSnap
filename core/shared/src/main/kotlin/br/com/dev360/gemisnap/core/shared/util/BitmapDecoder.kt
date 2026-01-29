package br.com.dev360.gemisnap.core.shared.util

import android.graphics.Bitmap
import android.net.Uri

interface BitmapDecoder {
    fun decode(uri: Uri): Bitmap
}