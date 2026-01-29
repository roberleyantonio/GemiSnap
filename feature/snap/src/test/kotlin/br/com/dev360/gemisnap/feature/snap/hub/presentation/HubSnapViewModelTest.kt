package br.com.dev360.gemisnap.feature.snap.hub.presentation

import android.net.Uri
import br.com.dev360.gemisnap.core.networking.data.ResultWrapper
import br.com.dev360.gemisnap.core.shared.coroutines.CoroutineTestRule
import br.com.dev360.gemisnap.core.shared.test.coVerifyNever
import br.com.dev360.gemisnap.core.shared.test.coVerifyOnce
import br.com.dev360.gemisnap.core.shared.test.relaxedMock
import br.com.dev360.gemisnap.feature.snap.BASE64_IMAGE
import br.com.dev360.gemisnap.feature.snap.ERROR_MESSAGE
import br.com.dev360.gemisnap.feature.snap.GEMINI_RESPONSE_TEXT
import br.com.dev360.gemisnap.feature.snap.PROMPT
import br.com.dev360.gemisnap.feature.snap.failureError
import br.com.dev360.gemisnap.feature.snap.geminiResultWrapperSuccess
import br.com.dev360.gemisnap.feature.snap.hub.domain.HubSnapContract
import io.mockk.coEvery
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HubSnapViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val repository: HubSnapContract.Repository = relaxedMock()
    private val uiModel: HubSnapContract.UiModel = relaxedMock()
    private val uri: Uri = relaxedMock()

    private val viewModel = HubSnapViewModel(repository, uiModel, coroutineTestRule.dispatchers)

    @Before
    fun setup() {
        confirmVerified(repository, uiModel)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `analyzeImage should update state to success when everything works`() =
        runTest(coroutineTestRule.testDispatcher) {
            every { uiModel.getDefaultPrompt(any()) } returns PROMPT
            coEvery { uiModel.decodeAndConvertBase64(uri) } returns BASE64_IMAGE
            coEvery {
                repository.generateContent(
                    any(),
                    BASE64_IMAGE
                )
            } returns geminiResultWrapperSuccess

            viewModel.onAction(HubSnapAction.ImageSelected(uri))
            viewModel.onAction(HubSnapAction.PrimaryActionClicked)
            advanceUntilIdle()

            coVerifyOnce { repository.generateContent(any(), any()) }

            val state = viewModel.uiState.value
            assertEquals(GEMINI_RESPONSE_TEXT, state.geminiText)
            assertTrue(state.errorMessage.isNullOrBlank())
            assertEquals(false, state.isLoading)
        }

    @Test
    fun `analyzeImage should set isError when repository fails`() =
        runTest(coroutineTestRule.testDispatcher) {
            every { uiModel.getDefaultPrompt(any()) } returns PROMPT
            coEvery { uiModel.decodeAndConvertBase64(uri) } returns BASE64_IMAGE

            coEvery { repository.generateContent(any(), any()) } returns ResultWrapper.Error(failureError)
            every { uiModel.getErrorMessage(failureError) } returns ERROR_MESSAGE

            viewModel.onAction(HubSnapAction.ImageSelected(uri))
            viewModel.onAction(HubSnapAction.PrimaryActionClicked)
            advanceUntilIdle()

            coVerifyOnce { repository.generateContent(PROMPT, BASE64_IMAGE) }

            val state = viewModel.uiState.value
            assertEquals(ERROR_MESSAGE, state.errorMessage)
            assertEquals(false, state.isLoading)
            assertEquals(null, state.geminiText)
        }

    @Test
    fun `analyzeImage should stop if image processing fails`() =
        runTest(coroutineTestRule.testDispatcher) {
            every { uiModel.getDefaultPrompt(any()) } returns PROMPT
            coEvery { uiModel.decodeAndConvertBase64(uri) } throws RuntimeException(ERROR_MESSAGE)

            viewModel.onAction(HubSnapAction.ImageSelected(uri))
            viewModel.onAction(HubSnapAction.PrimaryActionClicked)
            advanceUntilIdle()

            coVerifyNever { repository.generateContent(any(), any()) }

            val state = viewModel.uiState.value
            assertTrue(state.errorMessage.isNullOrBlank())
            assertEquals(false, state.isLoading)
        }
}