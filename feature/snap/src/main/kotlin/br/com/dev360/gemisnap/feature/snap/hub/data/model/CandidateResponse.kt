package br.com.dev360.gemisnap.feature.snap.hub.data.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CandidateResponse(
    @SerializedName("content")val content: ContentResponse? = null
)