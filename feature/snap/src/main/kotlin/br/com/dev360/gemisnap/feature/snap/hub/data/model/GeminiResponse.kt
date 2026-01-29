package br.com.dev360.gemisnap.feature.snap.hub.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    @SerializedName("candidates") val candidates: List<CandidateResponse>? = null
)


fun GeminiResponse.toText() = this.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text