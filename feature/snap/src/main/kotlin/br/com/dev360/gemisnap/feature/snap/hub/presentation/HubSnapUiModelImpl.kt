package br.com.dev360.gemisnap.feature.snap.hub.presentation

import android.content.res.Resources
import android.net.Uri
import br.com.dev360.gemisnap.core.networking.exception.Failure
import br.com.dev360.gemisnap.core.shared.extensions.toBase64
import br.com.dev360.gemisnap.core.shared.util.BitmapDecoder
import br.com.dev360.gemisnap.feature.snap.R
import br.com.dev360.gemisnap.feature.snap.hub.domain.HubSnapContract

class HubSnapUiModelImpl(
    private val resources: Resources,
    private val decoder: BitmapDecoder,
): HubSnapContract.UiModel {

    override fun getDefaultPrompt(prompt: String): String =
        prompt.ifBlank { resources.getString(R.string.default_prompt) }

    override fun decodeAndConvertBase64(uri: Uri): String {
        val bitmap = decoder.decode(uri)
        return try {
            bitmap.toBase64()
        } finally {
            bitmap.recycle()
        }
    }

    override fun getErrorMessage(failure: Failure): String {
        return when(failure) {
            is Failure.ServerError -> resources.getString(R.string.error_api_key)
            is Failure.NetworkError -> resources.getString(R.string.error_no_internet)
            else -> resources.getString(R.string.error_generic)
        }
    }

    override fun getDecodeImageErrorMessage(): String = resources.getString(R.string.error_decode_image)
}