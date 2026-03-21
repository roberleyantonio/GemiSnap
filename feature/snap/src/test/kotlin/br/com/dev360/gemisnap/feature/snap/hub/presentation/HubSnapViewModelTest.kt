package br.com.dev360.gemisnap.feature.snap.hub.presentation

import android.net.Uri
import app.cash.turbine.test
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
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HubSnapViewModelTest {
    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private val repository: HubSnapContract.Repository = relaxedMock()
    private val uiModel: HubSnapContract.UiModel = relaxedMock()
    private val uri: Uri = relaxedMock()

    private lateinit var viewModel: HubSnapViewModel

    @Before
    fun setup() {
        viewModel = HubSnapViewModel(repository, uiModel, coroutineTestRule.dispatchers)
        confirmVerified(repository, uiModel)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `analyzeImage should update state to success when everything works`() =
        runTest(coroutineTestRule.testDispatcher) {
            coEvery { uiModel.decodeAndConvertBase64(uri) } returns BASE64_IMAGE
            coEvery { repository.generateContent(any(), BASE64_IMAGE) } returns geminiResultWrapperSuccess

            viewModel.uiState.test {
                assertEquals(HubSnapState(), awaitItem())

                viewModel.onAction(HubSnapAction.ImageSelected(uri))
                assertEquals(HubSnapState(selectedImageUri = uri), awaitItem())

                viewModel.onAction(HubSnapAction.PrimaryActionClicked(PROMPT))

                val loadingState = awaitItem()
                assertTrue(loadingState.isLoading)
                assertNull(loadingState.errorMessage)

                val successState = awaitItem()
                assertFalse(successState.isLoading)
                assertEquals(GEMINI_RESPONSE_TEXT, successState.geminiText)

                cancelAndIgnoreRemainingEvents()
            }

            coVerifyOnce { repository.generateContent(any(), any()) }
        }

    @Test
    fun `analyzeImage should set isError when repository fails`() =
        runTest(coroutineTestRule.testDispatcher) {
            coEvery { uiModel.decodeAndConvertBase64(uri) } returns BASE64_IMAGE

            coEvery { repository.generateContent(any(), any()) } returns ResultWrapper.Error(failureError)
            every { uiModel.getErrorMessage(failureError) } returns ERROR_MESSAGE

            viewModel.uiState.test {
                skipItems(1)

                viewModel.onAction(HubSnapAction.ImageSelected(uri))
                awaitItem()

                viewModel.onAction(HubSnapAction.PrimaryActionClicked(PROMPT))
                assertTrue(awaitItem().isLoading)

                val errorState = awaitItem()
                assertFalse(errorState.isLoading)
                assertEquals(ERROR_MESSAGE, errorState.errorMessage)
                assertNull(errorState.geminiText)

                cancelAndIgnoreRemainingEvents()
            }

            coVerifyOnce { repository.generateContent(PROMPT, BASE64_IMAGE) }
        }

    @Test
    fun `analyzeImage should stop if image processing fails`() =
        runTest(coroutineTestRule.testDispatcher) {
            coEvery { uiModel.decodeAndConvertBase64(uri) } throws RuntimeException(ERROR_MESSAGE)
            every { uiModel.getDecodeImageErrorMessage() } returns ERROR_MESSAGE

            viewModel.uiState.test {
                skipItems(1)

                viewModel.onAction(HubSnapAction.ImageSelected(uri))
                awaitItem()

                viewModel.onAction(HubSnapAction.PrimaryActionClicked(PROMPT))
                assertTrue(awaitItem().isLoading)

                val errorState = awaitItem()
                assertFalse(errorState.isLoading)
                assertEquals(ERROR_MESSAGE, errorState.errorMessage)

                cancelAndIgnoreRemainingEvents()
            }

            coVerifyNever { repository.generateContent(any(), any()) }

        }
}