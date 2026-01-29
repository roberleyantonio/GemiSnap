package br.com.dev360.gemisnap.feature.snap.hub.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class InlineData(
    @SerializedName("mime_type") val mimeType: String? = null,
    @SerializedName("data") val data: String? = null
)