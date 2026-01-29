package br.com.dev360.gemisnap.feature.snap.hub.presentation

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.dev360.gemisnap.core.sharedui.components.FeedbackDataModel
import br.com.dev360.gemisnap.core.sharedui.components.FeedbackType
import br.com.dev360.gemisnap.core.sharedui.components.ScreenFeedback
import br.com.dev360.gemisnap.core.sharedui.theme.dimens
import br.com.dev360.gemisnap.feature.snap.R
import br.com.dev360.gemisnap.feature.snap.hub.presentation.components.HubSnapContent
import org.koin.androidx.compose.koinViewModel

private const val IMAGE_INPUT_TYPE = "image/*"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubSnapScreen(
    viewModel: HubSnapViewModel = koinViewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            viewModel.onAction(HubSnapAction.ImageSelected(it))
        }
    }

    state.errorMessage?.let {
        ScreenFeedback(
            modifier = Modifier.padding(top = dimens.margin_16),
            feedbackData = FeedbackDataModel(
                feedbackType = FeedbackType.ERROR,
                title = stringResource(R.string.gemini_error_title),
                subtitle = it,
                primaryButtonTitle = stringResource(R.string.gemini_primary_button_title),
                secondaryButtonTitle = stringResource(R.string.gemini_secondary_button_title)
            ),
            onPrimaryButtonClick = { viewModel.onAction(HubSnapAction.PrimaryActionClicked) },
            onSecondaryButtonClick = { viewModel.closeFeedbackError() }
        )
        return
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.hub_snap_screen_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { paddingValues ->
        HubSnapContent(
            imageUri = selectedImageUri,
            prompt = state.prompt,
            modifier = Modifier.padding(paddingValues),
            geminiText = state.geminiText,
            isLoading = state.isLoading,
            onImageClick = { launcher.launch(IMAGE_INPUT_TYPE) },
            onPromptButtonClick = {
                viewModel.onAction(HubSnapAction.PrimaryActionClicked)
            },
            onPromptChange = { viewModel.onAction(HubSnapAction.PromptChanged(it)) }
        )
    }
}