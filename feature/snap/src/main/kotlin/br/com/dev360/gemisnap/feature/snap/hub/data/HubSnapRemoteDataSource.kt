package br.com.dev360.gemisnap.feature.snap.hub.data

import br.com.dev360.gemisnap.core.networking.factory.NetworkResponse
import br.com.dev360.gemisnap.feature.snap.hub.data.model.GeminiRequest
import br.com.dev360.gemisnap.feature.snap.hub.data.model.GeminiResponse
import br.com.dev360.gemisnap.feature.snap.hub.domain.HubSnapContract

class HubSnapRemoteDataSourceImpl(
    private val service: SnapService
): HubSnapContract.RemoteDataSource {
    override suspend fun generateContent(apiKey: String, request: GeminiRequest): NetworkResponse<GeminiResponse> =
        service.generateContent(apiKey, request)

}