
package com.devarag.forge.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devarag.forge.ui.design.ForgeColors

@Composable
fun TaskItem(
    title: String,
    completed: Boolean,
    category: String = "General",
    dueDate: String = "Today",
    onCheckedChange: (Boolean) -> Unit = {},
    onDelete: () -> Unit = {}
) {
    var showDeleteConfirmation by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = ForgeColors.Surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = completed,
                    onCheckedChange = onCheckedChange,
                    colors = CheckboxDefaults.colors(
                        checkedColor = ForgeColors.Primary
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = ForgeColors.TextPrimary,
                        modifier = if (completed) {
                            Modifier.alpha(0.6f)
                        } else {
                            Modifier
                        }
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.labelMedium,
                            color = ForgeColors.Primary
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = dueDate,
                            style = MaterialTheme.typography.labelMedium,
                            color = ForgeColors.TextSecondary
                        )
                    }
                }

                IconButton(
                    onClick = {
                        showDeleteConfirmation = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Task",
                        tint = ForgeColors.TextSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider()
        }
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = {
                showDeleteConfirmation = false
            },
            title = {
                Text("Delete task?")
            },
            text = {
                Text(
                    "Are you sure you want to delete \"$title\"? " +
                            "This action cannot be undone."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                        onDelete()
                    }
                ) {
                    Text(
                        "Delete",
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirmation = false
                    }
                ) {
                    Text("Cancel")
                }
            },
            containerColor = ForgeColors.Surface,
            titleContentColor = ForgeColors.TextPrimary,
            textContentColor = ForgeColors.TextSecondary
        )
    }
}
