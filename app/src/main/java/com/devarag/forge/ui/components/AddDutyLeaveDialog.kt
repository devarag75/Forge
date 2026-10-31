
package com.devarag.forge.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.devarag.forge.data.entity.DutyLeave
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDutyLeaveDialog(
    onDismiss: () -> Unit,
    onSave: (DutyLeave) -> Unit
) {
    val startState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    val endState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    var eventName by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var fullDay by remember { mutableStateOf(true) }
    var selectedPeriods by remember { mutableStateOf(setOf<Int>()) }
    var approvalStatus by remember { mutableStateOf("PENDING") }

    fun formatDate(value: Long?): String {
        if (value == null) return "Select date"
        return SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(Date(value))
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Duty Leave") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = eventName,
                    onValueChange = { eventName = it },
                    label = { Text("Event name") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedButton(
                    onClick = { showStartPicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("From: ${formatDate(startState.selectedDateMillis)}")
                }

                OutlinedButton(
                    onClick = { showEndPicker = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("To: ${formatDate(endState.selectedDateMillis)}")
                }

                Text("Periods covered")

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = fullDay,
                        onClick = {
                            fullDay = true
                            selectedPeriods = emptySet()
                        },
                        label = { Text("Full day") }
                    )
                    FilterChip(
                        selected = !fullDay,
                        onClick = { fullDay = false },
                        label = { Text("Specific") }
                    )
                }

                if (!fullDay) {
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

                Text("Approval status")

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    listOf("PENDING", "APPROVED", "REJECTED").forEach { status ->
                        FilterChip(
                            selected = approvalStatus == status,
                            onClick = { approvalStatus = status },
                            label = { Text(status.lowercase().replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }

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
                enabled = eventName.isNotBlank() &&
                        startState.selectedDateMillis != null &&
                        endState.selectedDateMillis != null &&
                        endState.selectedDateMillis!! >= startState.selectedDateMillis!! &&
                        (fullDay || selectedPeriods.isNotEmpty()),
                onClick = {
                    val start = startState.selectedDateMillis ?: return@TextButton
                    val end = endState.selectedDateMillis ?: return@TextButton

                    onSave(
                        DutyLeave(
                            eventName = eventName.trim(),
                            startDate = start,
                            endDate = end,
                            hoursCoveredCsv = if (fullDay) ""
                            else selectedPeriods.sorted().joinToString(","),
                            reason = reason.trim(),
                            approvalStatus = approvalStatus,
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

    if (showStartPicker) {
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = { showStartPicker = false }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = startState)
        }
    }

    if (showEndPicker) {
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = { showEndPicker = false }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = endState)
        }
    }
}
