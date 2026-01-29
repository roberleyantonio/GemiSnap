package br.com.dev360.gemisnap.core.sharedui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import br.com.dev360.gemisnap.core.shared.extensions.empty
import br.com.dev360.gemisnap.core.sharedui.R


object FeedbackDefaults {
    @Composable
    internal fun feedbackIcon(type: FeedbackType): FeedbackIconData {
        return when(type) {
            FeedbackType.SUCCESS -> FeedbackIconData(
                painter = FeedbackIcons.defaults().success,
            )
            FeedbackType.WARNING -> FeedbackIconData(
                painter = FeedbackIcons.defaults().warning,
            )
            FeedbackType.ERROR -> FeedbackIconData(
                painter = FeedbackIcons.defaults().error,
            )
            FeedbackType.INFORMATIVE -> FeedbackIconData(
                painter = FeedbackIcons.defaults().informative,
            )
        }
    }
}

data class FeedbackDataModel(
    val feedbackType: FeedbackType = FeedbackType.SUCCESS,
    val title: String = String.empty(),
    val subtitle: String? = null,
    val primaryButtonTitle: String = String.empty(),
    val secondaryButtonTitle: String? = null,
    val errorType: Any? = null,
)

enum class FeedbackType {
    SUCCESS,
    WARNING,
    ERROR,
    INFORMATIVE,
}

internal class FeedbackIconData(
    val painter: Painter,
)

internal data class FeedbackIcons(
    val success: Painter,
    val error: Painter,
    val warning: Painter,
    val informative: Painter
) {
    companion object {
        @Composable
        fun defaults() = FeedbackIcons(
            success = painterResource(R.drawable.ic_feedback_success),
            error = painterResource(R.drawable.ic_feedback_error),
            warning = painterResource(R.drawable.ic_feedback_warning),
            informative = painterResource(R.drawable.ic_feedback_informative),
        )
    }
}