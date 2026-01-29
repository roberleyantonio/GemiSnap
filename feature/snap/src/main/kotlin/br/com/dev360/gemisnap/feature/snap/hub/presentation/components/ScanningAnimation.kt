package br.com.dev360.gemisnap.feature.snap.hub.presentation.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ScanningAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "scanning")
    val translateY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laser"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val strokeWidth = 4.dp.toPx()
        val yOffset = size.height * translateY

        // Efeito de gradiente atrás da linha
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Cyan.copy(alpha = 0.3f), Color.Transparent),
                startY = yOffset - 100f,
                endY = yOffset
            ),
            size = Size(size.width, 100f),
            topLeft = Offset(0f, yOffset - 100f)
        )

        // Linha do Laser
        drawLine(
            color = Color.Cyan,
            start = Offset(0f, yOffset),
            end = Offset(size.width, yOffset),
            strokeWidth = strokeWidth
        )
    }
}