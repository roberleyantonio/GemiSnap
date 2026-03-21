package br.com.dev360.gemisnap.feature.snap.hub.presentation

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.dev360.gemisnap.feature.snap.ERROR_MESSAGE
import br.com.dev360.gemisnap.feature.snap.R
import br.com.dev360.gemisnap.feature.snap.TEST_INPUT_PROMPT
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HubSnapScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val viewModel: HubSnapViewModel = mockk(relaxed = true)
    private val uiState = MutableStateFlow(HubSnapState())

    @Before
    fun setup() {
        every { viewModel.uiState } returns uiState
    }

    @Test
    fun shouldDisplayScreenTitle_whenThereIsNoError() {
        val titleText = composeTestRule.activity.getString(R.string.hub_snap_screen_title)

        composeTestRule.setContent {
            HubSnapScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText(titleText).assertIsDisplayed()
    }

    @Test
    fun shouldShowErrorFeedback_whenStateHasErrorMessage() {
        uiState.value = HubSnapState(errorMessage = ERROR_MESSAGE)

        composeTestRule.setContent {
            HubSnapScreen(viewModel = viewModel)
        }

        composeTestRule.onNodeWithText(ERROR_MESSAGE).assertIsDisplayed()

        val retryBtnText = composeTestRule.activity.getString(R.string.gemini_primary_button_title)
        composeTestRule.onNodeWithText(retryBtnText).assertIsDisplayed()
    }

    @Test
    fun whenErrorIsDisplayed_andRetryIsClicked_shouldTriggerAction() {
        uiState.value = HubSnapState(errorMessage = ERROR_MESSAGE)

        composeTestRule.setContent {
            HubSnapScreen(viewModel = viewModel)
        }

        val retryBtnText = composeTestRule.activity.getString(R.string.gemini_primary_button_title)
        composeTestRule.onNodeWithText(retryBtnText).performClick()

        verify { viewModel.onAction(HubSnapAction.RetryRequested) }
    }

    @Test
    fun whenCloseFeedbackIsClicked_shouldCallViewModelClose() {
        uiState.value = HubSnapState(errorMessage = ERROR_MESSAGE)

        composeTestRule.setContent {
            HubSnapScreen(viewModel = viewModel)
        }

        val cancelBtnText = composeTestRule.activity.getString(R.string.gemini_secondary_button_title)
        composeTestRule.onNodeWithText(cancelBtnText).performClick()

        verify { viewModel.closeFeedbackError() }
    }

    @Test
    fun whenImageIsSelected_andUserTypesPrompt_andClicksAnalyze_shouldPassTextToViewModel() {
        uiState.value = HubSnapState(
            selectedImageUri = mockk<Uri>(relaxed = true),
            isLoading = false,
            geminiText = null
        )

        composeTestRule.setContent {
            HubSnapScreen(viewModel = viewModel)
        }

        val promptLabel = composeTestRule.activity.getString(R.string.prompt_label)
        val analyzeBtnText = composeTestRule.activity.getString(R.string.get_from_gemini)

        composeTestRule.onNodeWithText(promptLabel).performTextInput(TEST_INPUT_PROMPT)

        composeTestRule.onNodeWithText(analyzeBtnText).performClick()

        verify {
            viewModel.onAction(
                HubSnapAction.PrimaryActionClicked(prompt = TEST_INPUT_PROMPT)
            )
        }
    }
}