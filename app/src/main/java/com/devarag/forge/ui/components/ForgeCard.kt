package com.devarag.forge.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarag.forge.ui.design.ForgeColors
import com.devarag.forge.ui.design.ForgeRadius
import androidx.compose.foundation.layout.padding

@Composable
fun ForgeCard(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    content: @Composable () -> Unit
) {

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = ForgeRadius.Medium,
        colors = CardDefaults.cardColors(
            containerColor = ForgeColors.Card
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {

        Box(
            modifier = Modifier.padding(contentPadding)
        ) {
            content()
        }

    }

}