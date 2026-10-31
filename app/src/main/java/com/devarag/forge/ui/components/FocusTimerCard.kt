package com.devarag.forge.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.devarag.forge.ui.design.ForgeColors

@Composable
fun FocusTimerCard(
    minutes: Int,
    seconds: Int
) {

    val totalSeconds = 25 * 60
    val currentSeconds = minutes * 60 + seconds

    val progress = currentSeconds.toFloat() / totalSeconds

    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(500),
        label = "progress"
    )

    Card(
        shape = RoundedCornerShape(22.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            val totalSeconds = 25 * 60
            val currentSeconds = minutes * 60 + seconds

            CircularProgressRing(
                progress = currentSeconds.toFloat() / totalSeconds,
                minutes = minutes,
                seconds = seconds
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Focus Session",
                style = MaterialTheme.typography.bodyLarge,
                color = ForgeColors.TextSecondary
            )
            Spacer(modifier = Modifier.height(28.dp))

            SessionStats()

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}