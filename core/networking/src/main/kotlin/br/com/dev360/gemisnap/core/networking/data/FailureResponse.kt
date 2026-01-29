package br.com.dev360.gemisnap.core.networking.data

import com.google.gson.annotations.SerializedName

data class FailureResponse(
    @SerializedName("code")
    val code: String? = null,

    @SerializedName("message")
    val details: String? = null,

) {
    fun getCodeResponse(): String? = code

    fun getMessageResponse(): String? = details
}
