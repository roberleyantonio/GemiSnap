package br.com.dev360.gemisnap.core.shared.extensions

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

private const val IMAGE_QUALITY = 80

fun Bitmap.toBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}