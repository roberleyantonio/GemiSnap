package br.com.dev360.gemisnap.core.sharedui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import br.com.dev360.gemisnap.core.sharedui.extensions.singleClick
import br.com.dev360.gemisnap.core.sharedui.theme.GemiSnapTheme
import br.com.dev360.gemisnap.core.sharedui.theme.Typography
import br.com.dev360.gemisnap.core.sharedui.theme.dimens
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
@Composable
fun CustomTwoButtons(
    modifier: Modifier = Modifier,
    primaryButtonModifier: Modifier = Modifier,
    secondaryButtonModifier: Modifier = Modifier,
    buttonBorder: BorderStroke? = BorderStroke(dimens.size_1, MaterialTheme.colorScheme.secondary),
    primaryTitle: String,
    primaryStatus: Boolean = true,
    onPrimaryClick: () -> Unit,
    secondaryTitle: String? = null,
    secondaryStatus: Boolean = true,
    onSecondaryClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
    ) {
        secondaryTitle?.let {
            TextButton(
                modifier = secondaryButtonModifier
                    .weight(1f)
                    .fillMaxWidth()
                    .singleClick { onSecondaryClick?.invoke() },
                border = buttonBorder,
                shape = RoundedCornerShape(dimens.margin_8),
                enabled = secondaryStatus,
                onClick = { },
            ) {
                Text(
                    text = secondaryTitle,
                    style = Typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
            }
        } ?: Spacer(modifier = Modifier
            .weight(1f)
            .fillMaxWidth())

        Spacer(modifier = Modifier.width(dimens.margin_16))

        Button(
            modifier = primaryButtonModifier
                .weight(1f)
                .fillMaxWidth()
                .singleClick { onPrimaryClick() },
            border = buttonBorder,
            shape = RoundedCornerShape(dimens.margin_8),
            colors = ButtonDefaults.buttonColors().copy(
                MaterialTheme.colorScheme.onPrimary
            ),
            enabled = primaryStatus,
            onClick = {}
        ) {
            Text(
                text = primaryTitle,
                style = Typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CustomTwoButtonsPreview() {
    GemiSnapTheme {
        CustomTwoButtons(
            primaryTitle = "Button one",
            onPrimaryClick = {},
            secondaryTitle = "Button two",
            onSecondaryClick = {},
        )
    }
}