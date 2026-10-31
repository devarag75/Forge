package com.devarag.forge.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarag.forge.ui.design.ForgeColors

@Composable
fun SessionStats() {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        StatItem(
            title = "Today's Focus",
            value = "2h 13m"
        )

        StatItem(
            title = "Sessions",
            value = "5"
        )

        StatItem(
            title = "Streak",
            value = "4"
        )
    }

}

@Composable
private fun StatItem(
    title: String,
    value: String
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            color = ForgeColors.TextPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = ForgeColors.TextSecondary
        )

    }

}