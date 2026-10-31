package com.devarag.forge.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarag.forge.ui.design.ForgeColors

@Composable
fun StatCard(
    title: String,
    value: String
) {

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(120.dp),
        shape = RoundedCornerShape(18.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = ForgeColors.TextSecondary
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                color = ForgeColors.TextPrimary
            )

        }

    }

}