package br.com.dev360.gemisnap.core.sharedui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import br.com.dev360.gemisnap.core.sharedui.theme.GemiSnapTheme
import br.com.dev360.gemisnap.core.sharedui.theme.Typography
import br.com.dev360.gemisnap.core.sharedui.theme.dimens

@Composable
fun ScreenFeedback(
    modifier: Modifier = Modifier,
    feedbackData: FeedbackDataModel?,
    onPrimaryButtonClick: () -> Unit,
    onSecondaryButtonClick: (() -> Unit)? = null,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    if (feedbackData == null) return
    val feedbackIcon = FeedbackDefaults.feedbackIcon(feedbackData.feedbackType)

    LaunchedEffect(Unit) {
        keyboardController?.hide()
        focusManager.clearFocus()
    }

    Column(
        modifier = modifier
            .padding(horizontal = dimens.margin_16)
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(dimens.margin_36))

            Image(
                modifier = Modifier
                    .size(dimens.size_150),
                painter = feedbackIcon.painter,
                contentDescription = null,
            )

            Spacer(modifier = Modifier.height(dimens.margin_36))

            Text(
                text = feedbackData.title,
                style = Typography.titleLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            )

            feedbackData.subtitle?.let {
                Spacer(modifier = Modifier.height(dimens.margin_16))

                Text(
                    text = it,
                    style = Typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary                )
            }
        }

        Spacer(modifier = Modifier.height(dimens.margin_16))

        CustomTwoButtons(
            modifier = Modifier
                .padding(
                    horizontal = dimens.margin_8
                )
                .navigationBarsPadding(),
            primaryTitle = feedbackData.primaryButtonTitle,
            onPrimaryClick = onPrimaryButtonClick,
            secondaryTitle = feedbackData.secondaryButtonTitle,
            onSecondaryClick = { onSecondaryButtonClick?.invoke() },
        )

        Spacer(modifier = Modifier.height(dimens.margin_24))
    }

}

@Preview
@Composable
private fun ScreenFeedbackPreview() {
    GemiSnapTheme {
        ScreenFeedback(
            feedbackData = FeedbackDataModel(
                title = "Title",
                subtitle = null,
                primaryButtonTitle = "Primary button",
                secondaryButtonTitle = "Secondary button",
            ),
            onPrimaryButtonClick = { },
            onSecondaryButtonClick = { },
        )
    }
}