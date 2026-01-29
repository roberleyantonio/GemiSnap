package br.com.dev360.gemisnap.feature.snap

import br.com.dev360.gemisnap.core.networking.exception.Failure
import br.com.dev360.gemisnap.core.networking.factory.NetworkResponse
import br.com.dev360.gemisnap.core.networking.factory.toResult
import br.com.dev360.gemisnap.feature.snap.hub.data.model.CandidateResponse
import br.com.dev360.gemisnap.feature.snap.hub.data.model.ContentResponse
import br.com.dev360.gemisnap.feature.snap.hub.data.model.GeminiResponse
import br.com.dev360.gemisnap.feature.snap.hub.data.model.PartTextResponse
import br.com.dev360.gemisnap.feature.snap.hub.data.model.toText

const val PROMPT = "Analyze this image"
const val BASE64_IMAGE = "base64-encoded-data"
const val GEMINI_RESPONSE_TEXT = "Analysis Complete"

const val IMAGE_TYPE = "image/jpeg"
const val ERROR_MESSAGE = "API Error"
const val EXPECTED_API_KEY = BuildConfig.GEMINI_API_KEY

val geminiResponse = GeminiResponse(
    candidates = listOf(
        CandidateResponse(
            content = ContentResponse(
                parts = listOf(
                    PartTextResponse(text = GEMINI_RESPONSE_TEXT)
                )
            )
        )
    )
)

val geminiNetworkResponseSuccess = NetworkResponse.Success(geminiResponse)

val geminiResultWrapperSuccess = geminiNetworkResponseSuccess.toResult().map { it.toText() }

val failureError = Failure.GenericError(ERROR_MESSAGE)