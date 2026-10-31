package com.devarag.forge.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.devarag.forge.ui.design.ForgeColors

@Composable
fun ProductivityCard() {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = ForgeColors.Card
        )
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(
                            ForgeColors.Card,
                            ForgeColors.Background
                        )
                    )
                )
                .padding(24.dp)
        ) {

            Column {

                Text(
                    text = "Good Evening 👋",
                    style = MaterialTheme.typography.titleMedium,
                    color = ForgeColors.TextSecondary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Devarag",
                    style = MaterialTheme.typography.headlineMedium,
                    color = ForgeColors.TextPrimary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Productivity Score",
                    style = MaterialTheme.typography.bodyMedium,
                    color = ForgeColors.TextSecondary
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "92%",
                    style = MaterialTheme.typography.displayLarge,
                    color = ForgeColors.Primary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "You're doing great today! 🔥",
                    style = MaterialTheme.typography.bodyLarge,
                    color = ForgeColors.TextPrimary
                )

            }

        }

    }
}