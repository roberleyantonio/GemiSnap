package br.com.dev360.gemisnap.feature.snap.hub.data

import br.com.dev360.gemisnap.core.shared.test.coVerifyOnce
import br.com.dev360.gemisnap.feature.snap.BASE64_IMAGE
import br.com.dev360.gemisnap.feature.snap.BuildConfig
import br.com.dev360.gemisnap.feature.snap.ERROR_MESSAGE
import br.com.dev360.gemisnap.feature.snap.EXPECTED_API_KEY
import br.com.dev360.gemisnap.feature.snap.GEMINI_RESPONSE_TEXT
import br.com.dev360.gemisnap.feature.snap.IMAGE_TYPE
import br.com.dev360.gemisnap.feature.snap.PROMPT
import br.com.dev360.gemisnap.feature.snap.geminiResponse
import br.com.dev360.gemisnap.feature.snap.hub.data.model.GeminiRequest
import br.com.dev360.gemisnap.feature.snap.hub.domain.HubSnapContract
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.slot
import io.mockk.unmockkAll
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HubSnapRepositoryImplTest {
    private lateinit var repository: HubSnapRepositoryImpl
    private val remoteDataSource: HubSnapContract.RemoteDataSource = mockk()

    @Before
    fun setup() {
        repository = HubSnapRepositoryImpl(remoteDataSource)
        clearAllMocks()
    }

    @After
    fun tearDown() {
        confirmVerified(remoteDataSource)
        unmockkAll()
    }

    @Test
    fun `generateContent builds correct request and returns response`() = runTest {
        val requestSlot = slot<GeminiRequest>()

        coEvery {
            remoteDataSource.generateContent(
                apiKey = BuildConfig.GEMINI_API_KEY,
                request = capture(requestSlot)
            )
        } returns geminiResponse

        val result = repository.generateContent(PROMPT, BASE64_IMAGE)

        assertEquals(GEMINI_RESPONSE_TEXT, result)

        val capturedRequest = requestSlot.captured
        val content = capturedRequest.contents.firstOrNull()
        val parts = content?.parts

        coVerifyOnce {
            remoteDataSource.generateContent(
                apiKey = EXPECTED_API_KEY,
                request = any()
            )
        }

        assertEquals(PROMPT, parts?.getOrNull(0)?.text)
        assertEquals(IMAGE_TYPE, parts?.getOrNull(1)?.inlineData?.mimeType)
        assertEquals(BASE64_IMAGE, parts?.getOrNull(1)?.inlineData?.data)
    }

    @Test(expected = Exception::class)
    fun `generateContent should throw exception when remoteDataSource fails`() = runTest {
        coEvery {
            remoteDataSource.generateContent(any(), any())
        } throws Exception(ERROR_MESSAGE)

        try {
            repository.generateContent(PROMPT, BASE64_IMAGE)
        } finally {
            coVerifyOnce {
                remoteDataSource.generateContent(
                    apiKey = any(),
                    request = any()
                )
            }
        }
    }

    @Test
    fun `generateContent should call remoteDataSource and return the flow`() = runTest {
        coEvery {
            remoteDataSource.generateContent(apiKey = any(), request = any())
        } returns geminiResponse

        val result = repository.generateContent(PROMPT, BASE64_IMAGE)

        assertEquals(GEMINI_RESPONSE_TEXT, result)

        coVerifyOnce {
            remoteDataSource.generateContent(
                apiKey = EXPECTED_API_KEY,
                request = any()
            )
        }
    }
}