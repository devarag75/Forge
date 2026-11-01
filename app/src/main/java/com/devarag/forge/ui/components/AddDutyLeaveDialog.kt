
package com.devarag.forge.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.devarag.forge.data.entity.DutyLeave
import com.devarag.forge.ui.design.ForgeColors
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

        return SimpleDateFormat(
            "dd MMM yyyy",
            Locale.getDefault()
        ).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }.format(Date(value))
    }

    val validDateRange =
        startState.selectedDateMillis != null &&
                endState.selectedDateMillis != null &&
                endState.selectedDateMillis!! >= startState.selectedDateMillis!!

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .heightIn(max = 760.dp),
            shape = RoundedCornerShape(26.dp),
            color = ForgeColors.Surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(17.dp)
            ) {
                // HEADER
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "COLLEGE ACTIVITY",
                        color = ForgeColors.Primary,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Add Duty Leave",
                        color = ForgeColors.TextPrimary,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Record a college event, tournament or official activity.",
                        color = ForgeColors.TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                HorizontalDivider(
                    color = ForgeColors.TextSecondary.copy(alpha = 0.15f)
                )

                // EVENT NAME
                Column(
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    DutySectionTitle(
                        "Event details",
                        "Enter the name of the activity"
                    )

                    OutlinedTextField(
                        value = eventName,
                        onValueChange = { eventName = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("e.g. University Football Tournament")
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = dutyTextFieldColors()
                    )
                }

                // DATE RANGE
                Column(
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    DutySectionTitle(
                        "Event dates",
                        "Choose the start and end dates"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        DutyDateCard(
                            title = "FROM",
                            date = formatDate(startState.selectedDateMillis),
                            modifier = Modifier.weight(1f),
                            onClick = { showStartPicker = true }
                        )

                        DutyDateCard(
                            title = "TO",
                            date = formatDate(endState.selectedDateMillis),
                            modifier = Modifier.weight(1f),
                            onClick = { showEndPicker = true }
                        )
                    }

                    if (!validDateRange) {
                        Text(
                            text = "End date must be on or after the start date.",
                            color = ForgeColors.Error,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }

                // PERIODS
                Column(
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    DutySectionTitle(
                        "Periods covered",
                        "Select full day or specific periods"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        DutyTypeCard(
                            title = "Full day",
                            selected = fullDay,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                fullDay = true
                                selectedPeriods = emptySet()
                            }
                        )

                        DutyTypeCard(
                            title = "Specific periods",
                            selected = !fullDay,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                fullDay = false
                            }
                        )
                    }

                    if (!fullDay) {
                        (1..7).chunked(4).forEach { group ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                group.forEach { period ->
                                    FilterChip(
                                        selected = period in selectedPeriods,
                                        onClick = {
                                            selectedPeriods =
                                                if (period in selectedPeriods) {
                                                    selectedPeriods - period
                                                } else {
                                                    selectedPeriods + period
                                                }
                                        },
                                        label = {
                                            Text("P$period")
                                        },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = ForgeColors.Primary,
                                            selectedLabelColor = ForgeColors.TextPrimary,
                                            containerColor = ForgeColors.Card,
                                            labelColor = ForgeColors.TextSecondary
                                        )
                                    )
                                }
                            }
                        }

                        Text(
                            text = if (selectedPeriods.isEmpty()) {
                                "No periods selected"
                            } else {
                                "${selectedPeriods.size} period(s) selected"
                            },
                            color = ForgeColors.Primary,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }

                // APPROVAL STATUS
                Column(
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    DutySectionTitle(
                        "Approval status",
                        "Update the status of your duty leave"
                    )

                    listOf(
                        listOf("PENDING", "APPROVED"),
                        listOf("REJECTED")
                    ).forEach { rowStatuses ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowStatuses.forEach { status ->
                                DutyStatusCard(
                                    status = status,
                                    selected = approvalStatus == status,
                                    modifier = if (rowStatuses.size == 1) {
                                        Modifier.fillMaxWidth()
                                    } else {
                                        Modifier.weight(1f)
                                    },
                                    onClick = {
                                        approvalStatus = status
                                    }
                                )
                            }
                        }
                    }
                }

                // REASON
                Column(
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    DutySectionTitle(
                        "Reason",
                        "Add the purpose of your duty leave (Optional)"
                    )

                    OutlinedTextField(
                        value = reason,
                        onValueChange = { reason = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("e.g. Representing college in a tournament")
                        },
                        minLines = 2,
                        shape = RoundedCornerShape(14.dp),
                        colors = dutyTextFieldColors()
                    )
                }

                // NOTES
                Column(
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    DutySectionTitle(
                        "Additional notes",
                        "Add any extra information (Optional)"
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Add details...")
                        },
                        minLines = 2,
                        shape = RoundedCornerShape(14.dp),
                        colors = dutyTextFieldColors()
                    )
                }

                HorizontalDivider(
                    color = ForgeColors.TextSecondary.copy(alpha = 0.15f)
                )

                // ACTIONS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = ForgeColors.TextSecondary
                        )
                    ) {
                        Text("Cancel")
                    }

                    Button(
                        onClick = {
                            val start = startState.selectedDateMillis
                                ?: return@Button
                            val end = endState.selectedDateMillis
                                ?: return@Button

                            onSave(
                                DutyLeave(
                                    eventName = eventName.trim(),
                                    startDate = start,
                                    endDate = end,
                                    hoursCoveredCsv = if (fullDay) {
                                        ""
                                    } else {
                                        selectedPeriods.sorted().joinToString(",")
                                    },
                                    reason = reason.trim(),
                                    approvalStatus = approvalStatus,
                                    notes = notes.trim()
                                )
                            )
                        },
                        enabled = eventName.isNotBlank() &&
                                validDateRange &&
                                (fullDay || selectedPeriods.isNotEmpty()),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForgeColors.Primary,
                            disabledContainerColor = ForgeColors.Primary.copy(alpha = 0.3f)
                        )
                    ) {
                        Text(
                            "Save Duty Leave",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }

    // START DATE PICKER
    if (showStartPicker) {
        DatePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(
                    onClick = { showStartPicker = false }
                ) {
                    Text(
                        "Confirm",
                        color = ForgeColors.Primary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showStartPicker = false }
                ) {
                    Text(
                        "Cancel",
                        color = ForgeColors.TextSecondary
                    )
                }
            }
        ) {
            DatePicker(state = startState)
        }
    }

    // END DATE PICKER
    if (showEndPicker) {
        DatePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(
                    onClick = { showEndPicker = false }
                ) {
                    Text(
                        "Confirm",
                        color = ForgeColors.Primary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showEndPicker = false }
                ) {
                    Text(
                        "Cancel",
                        color = ForgeColors.TextSecondary
                    )
                }
            }
        ) {
            DatePicker(state = endState)
        }
    }
}

@Composable
private fun DutySectionTitle(
    title: String,
    subtitle: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        Text(
            text = title,
            color = ForgeColors.TextPrimary,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold
        )

        Text(
            text = subtitle,
            color = ForgeColors.TextSecondary,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun DutyDateCard(
    title: String,
    date: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        color = ForgeColors.Card,
        border = BorderStroke(
            1.dp,
            ForgeColors.TextSecondary.copy(alpha = 0.15f)
        )
    ) {
        Column(
            modifier = Modifier.padding(13.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(
                text = title,
                color = ForgeColors.Primary,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = date,
                color = ForgeColors.TextPrimary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = "Change date",
                color = ForgeColors.TextSecondary,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun DutyTypeCard(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        color = if (selected) {
            ForgeColors.Primary.copy(alpha = 0.17f)
        } else {
            ForgeColors.Card
        },
        border = BorderStroke(
            if (selected) 1.5.dp else 1.dp,
            if (selected) {
                ForgeColors.Primary
            } else {
                ForgeColors.TextSecondary.copy(alpha = 0.15f)
            }
        )
    ) {
        Text(
            text = if (selected) "✓ $title" else title,
            modifier = Modifier.padding(13.dp),
            color = if (selected) {
                ForgeColors.Primary
            } else {
                ForgeColors.TextPrimary
            },
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DutyStatusCard(
    status: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val accent = when (status) {
        "APPROVED" -> ForgeColors.Success
        "REJECTED" -> ForgeColors.Error
        else -> ForgeColors.Warning
    }

    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(13.dp),
        color = if (selected) {
            accent.copy(alpha = 0.16f)
        } else {
            ForgeColors.Card
        },
        border = BorderStroke(
            if (selected) 1.5.dp else 1.dp,
            if (selected) {
                accent
            } else {
                ForgeColors.TextSecondary.copy(alpha = 0.15f)
            }
        )
    ) {
        Text(
            text = if (selected) "✓ ${status.lowercase().replaceFirstChar { it.uppercase() }}" else status.lowercase().replaceFirstChar { it.uppercase() },
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 12.dp
            ),
            color = if (selected) accent else ForgeColors.TextSecondary,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun dutyTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = ForgeColors.TextPrimary,
        unfocusedTextColor = ForgeColors.TextPrimary,
        focusedBorderColor = ForgeColors.Primary,
        unfocusedBorderColor = ForgeColors.TextSecondary.copy(alpha = 0.35f),
        focusedLabelColor = ForgeColors.Primary,
        unfocusedLabelColor = ForgeColors.TextSecondary,
        cursorColor = ForgeColors.Primary,
        focusedPlaceholderColor = ForgeColors.TextSecondary,
        unfocusedPlaceholderColor = ForgeColors.TextSecondary
    )
