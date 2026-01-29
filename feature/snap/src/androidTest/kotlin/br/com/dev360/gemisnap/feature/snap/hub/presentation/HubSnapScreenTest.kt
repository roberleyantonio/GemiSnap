package br.com.dev360.gemisnap.feature.snap.hub.presentation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import br.com.dev360.gemisnap.feature.snap.ERROR_MESSAGE
import br.com.dev360.gemisnap.feature.snap.R
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class HubSnapScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val viewModel: HubSnapViewModel = mockk(relaxed = true)
    private val uiState = MutableStateFlow(HubSnapState())

    @Before
    fun setup() {
        startKoin {
            modules(module {
                single { viewModel }
            })
        }
        every { viewModel.uiState } returns uiState
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun shouldDisplayScreenTitle() {
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

        verify { viewModel.onAction(HubSnapAction.PrimaryActionClicked) }
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

}