package br.com.dev360.gemisnap.core.shared.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

class AndroidBitmapDecoder(
    private val contentResolver: ContentResolver
) : BitmapDecoder {
    override fun decode(uri: Uri): Bitmap {
        val resolver = contentResolver
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(resolver, uri)
            ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
                if (info.size.width > IMAGE_MAX_SIZE || info.size.height > IMAGE_MAX_SIZE) {
                    decoder.setTargetSize(info.size.width / HALF_SIZE, info.size.height / HALF_SIZE)
                }
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
            }
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(resolver, uri)
        }
    }
    private companion object {
        const val IMAGE_MAX_SIZE = 2048
        const val HALF_SIZE = 2
    }
}