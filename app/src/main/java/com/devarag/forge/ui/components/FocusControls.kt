package com.devarag.forge.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FocusControls(

    isRunning: Boolean,

    onStart: () -> Unit,

    onPause: () -> Unit,

    onReset: () -> Unit

) {

    if (isRunning) {

        PrimaryButton(
            text = "⏸ Pause Session",
            onClick = onPause
        )

    } else {

        Column {

            PrimaryButton(
                text = "▶ Start / Resume",
                onClick = onStart
            )

            Spacer(modifier = Modifier.height(12.dp))

            PrimaryButton(
                text = "↻ Reset Session",
                onClick = onReset
            )

        }

    }

}