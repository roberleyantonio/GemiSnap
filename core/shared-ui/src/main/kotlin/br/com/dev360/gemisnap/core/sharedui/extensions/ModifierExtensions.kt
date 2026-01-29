package br.com.dev360.gemisnap.core.sharedui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Modifier.singleClick(
    debounceTime: Long = 1000L,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = this.then(
    Modifier.pointerInput(Unit) {
        awaitPointerEventScope {
            var lastClickTime by mutableLongStateOf(0L)
            while (true) {
                val event = awaitPointerEvent()
                if (event.type == PointerEventType.Press && enabled) {
                    val currentTime = System.currentTimeMillis()
                    if (currentTime - lastClickTime >= debounceTime) {
                        onClick()
                        lastClickTime = currentTime
                    }
                }
            }
        }
    }
)