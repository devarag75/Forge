package com.devarag.forge.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarag.forge.ui.design.ForgeColors
import androidx.compose.material.icons.filled.Info

@Composable
fun QuoteCard(

    quote: String,

    author: String

) {

    Card(

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(18.dp)

    ) {

        Column(

            modifier = Modifier.padding(20.dp)

        ) {

            Row {

                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = ForgeColors.Primary
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(

                    text = "Daily Quote",

                    style = MaterialTheme.typography.titleMedium,

                    color = ForgeColors.TextPrimary

                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(

                text = "\"$quote\"",

                style = MaterialTheme.typography.bodyLarge,

                color = ForgeColors.TextPrimary

            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(

                text = "— $author",

                style = MaterialTheme.typography.bodyMedium,

                color = ForgeColors.TextSecondary

            )

        }

    }

}