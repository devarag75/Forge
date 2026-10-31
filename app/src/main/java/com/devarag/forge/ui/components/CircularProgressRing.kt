package com.devarag.forge.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.devarag.forge.ui.design.ForgeColors
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun CircularProgressRing(

    progress: Float,

    minutes: Int,

    seconds: Int

) {

    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(500),
        label = "RingAnimation"
    )

    Box(
        modifier = Modifier.size(260.dp),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {

            val strokeWidth = 18.dp.toPx()

            // Background Ring
            drawArc(brush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFF7C4DFF),
                    Color(0xFF5C6CFF),
                    Color(0xFF3D8BFF),
                    Color(0xFF7C4DFF)
                )
            ),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )

            // Progress Ring
            drawArc(brush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFF7C4DFF),
                    Color(0xFF5C6CFF),
                    Color(0xFF3D8BFF),
                    Color(0xFF7C4DFF)
                )
            ),
                startAngle = -90f,
                sweepAngle = animatedProgress.value * 360f,
                useCenter = false,
                style = Stroke(
                    width = strokeWidth,
                    cap = StrokeCap.Round
                )
            )
        }

        Text(
            text = String.format("%02d:%02d", minutes, seconds),
            style = MaterialTheme.typography.displayLarge,
            color = ForgeColors.TextPrimary
        )
    }
}