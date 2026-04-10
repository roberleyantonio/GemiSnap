package br.com.dev360.gemisnap.feature.snap.hub.presentation.components

import android.content.ClipData
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.toClipEntry
import androidx.compose.ui.res.stringResource
import br.com.dev360.gemisnap.core.sharedui.theme.dimens
import br.com.dev360.gemisnap.feature.snap.R
import kotlinx.coroutines.launch

@Composable
fun HubSnapContent(
    imageUri: Uri?,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    geminiText: String? = null,
    onImageClick: () -> Unit,
    onAnalyzeClick: (String) -> Unit,
    onClearClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()
    val clipboardManager = LocalClipboard.current
    val context = LocalContext.current
    val textCopied = stringResource(R.string.text_copied)
    var localPrompt by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(dimens.margin_16),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ImageCard(
            model = imageUri,
            isLoading = isLoading,
            onClick = { onImageClick() }
        )

        Spacer(modifier = Modifier.height(dimens.margin_16))

        PromptField(
            prompt = localPrompt,
            enabled = isLoading.not(),
            visible =  imageUri != null,
            onValueChange = { localPrompt = it}
        )

        PromptButton(
            titleRes = if (geminiText.isNullOrBlank()) R.string.get_from_gemini else R.string.start_again,
            enabled = imageUri != null && isLoading.not(),
            isLoading = isLoading,
            onClick = {
                if (geminiText.isNullOrBlank()) {
                    onAnalyzeClick(localPrompt)
                } else {
                    localPrompt = ""
                    onClearClick()
                }
            }
        )
        Spacer(modifier = Modifier.height(dimens.margin_24))

        geminiText?.let { text ->
            SuccessResultComponent(
                text = text,
                onCopy = {
                    scope.launch {
                        val clipData =
                            ClipData.newPlainText(textCopied, text)
                        clipboardManager.setClipEntry(clipData.toClipEntry())
                        Toast.makeText(context, textCopied, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    }
}