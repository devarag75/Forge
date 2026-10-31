
package com.devarag.forge.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarag.forge.data.entity.Absence
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAbsenceDialog(
    onDismiss: () -> Unit,
    onSave: (Absence) -> Unit
) {
    val dateState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    var showDatePicker by remember { mutableStateOf(false) }
    var fullDay by remember { mutableStateOf(true) }
    var selectedPeriods by remember { mutableStateOf(setOf<Int>()) }
    var reason by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Mark Absence") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        dateState.selectedDateMillis?.let {
                            java.text.SimpleDateFormat(
                                "dd MMM yyyy",
                                java.util.Locale.getDefault()
                            ).apply {
                                timeZone = java.util.TimeZone.getTimeZone("UTC")
                            }.format(java.util.Date(it))
                        } ?: "Select date"
                    )
                }

                Text("Absence type")

                Row {
                    FilterChip(
                        selected = fullDay,
                        onClick = {
                            fullDay = true
                            selectedPeriods = emptySet()
                        },
                        label = { Text("Full day") }
                    )

                    Spacer(Modifier.width(8.dp))

                    FilterChip(
                        selected = !fullDay,
                        onClick = { fullDay = false },
                        label = { Text("Specific periods") }
                    )
                }

                if (!fullDay) {
                    Text("Select periods")

                    (1..7).chunked(4).forEach { group ->
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            group.forEach { period ->
                                FilterChip(
                                    selected = period in selectedPeriods,
                                    onClick = {
                                        selectedPeriods =
                                            if (period in selectedPeriods)
                                                selectedPeriods - period
                                            else
                                                selectedPeriods + period
                                    },
                                    label = { Text("$period") }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Reason (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = dateState.selectedDateMillis != null &&
                        (fullDay || selectedPeriods.isNotEmpty()),
                onClick = {
                    val date = dateState.selectedDateMillis ?: return@TextButton

                    onSave(
                        Absence(
                            date = date,
                            periodNumbersCsv = if (fullDay) ""
                            else selectedPeriods.sorted().joinToString(","),
                            reason = reason.trim(),
                            notes = notes.trim()
                        )
                    )
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }
}
