package br.com.dev360.gemisnap.feature.snap.hub.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import br.com.dev360.gemisnap.core.sharedui.theme.dimens
import br.com.dev360.gemisnap.feature.snap.R

private const val MAX_LINE_PROMPT = 3

@Composable
fun PromptField(
    prompt: String,
    enabled: Boolean = true,
    visible: Boolean = true,
    onValueChange: (String) -> Unit
) {
    if (visible.not()) return

    OutlinedTextField(
        value = prompt,
        onValueChange = { onValueChange(it) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.prompt_label)) },
        placeholder = { Text(stringResource(R.string.prompt_placeholder_text)) },
        enabled = enabled,
        singleLine = false,
        maxLines = MAX_LINE_PROMPT
    )
    Spacer(modifier = Modifier.height(dimens.margin_16))
}