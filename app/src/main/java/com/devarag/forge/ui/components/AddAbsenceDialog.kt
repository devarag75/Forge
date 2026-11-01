
package com.devarag.forge.ui.components

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
import com.devarag.forge.data.entity.Absence
import com.devarag.forge.ui.design.ForgeColors
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

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

    val selectedDate = dateState.selectedDateMillis?.let {
        SimpleDateFormat("EEEE, dd MMM yyyy", Locale.getDefault())
            .apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            .format(Date(it))
    } ?: "Select absence date"

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .heightIn(max = 720.dp),
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
                        text = "MARK ABSENCE",
                        color = ForgeColors.Primary,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Record an absence",
                        color = ForgeColors.TextPrimary,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Add the date and details of your missed classes.",
                        color = ForgeColors.TextSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                HorizontalDivider(
                    color = ForgeColors.TextSecondary.copy(alpha = 0.15f)
                )

                // DATE
                Column(
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    FormSectionTitle("Absence date", "Choose the day you were absent")

                    Surface(
                        onClick = { showDatePicker = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = ForgeColors.Card
                    ) {
                        Row(
                            modifier = Modifier.padding(15.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "SELECTED DATE",
                                    color = ForgeColors.TextSecondary,
                                    style = MaterialTheme.typography.labelSmall
                                )

                                Text(
                                    text = selectedDate,
                                    color = ForgeColors.TextPrimary,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Text(
                                text = "CHANGE",
                                color = ForgeColors.Primary,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // ABSENCE TYPE
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FormSectionTitle(
                        "Absence type",
                        "Choose how much of the day you missed"
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(9.dp)
                    ) {
                        AbsenceTypeCard(
                            title = "Full day",
                            subtitle = "All periods",
                            selected = fullDay,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                fullDay = true
                                selectedPeriods = emptySet()
                            }
                        )

                        AbsenceTypeCard(
                            title = "Specific",
                            subtitle = "Selected periods",
                            selected = !fullDay,
                            modifier = Modifier.weight(1f),
                            onClick = {
                                fullDay = false
                            }
                        )
                    }
                }

                // PERIOD SELECTION
                if (!fullDay) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        FormSectionTitle(
                            "Select periods",
                            "Choose one or more periods"
                        )

                        (1..7).chunked(4).forEach { group ->
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(9.dp)
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

                // REASON
                Column(
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    FormSectionTitle(
                        "Reason",
                        "Why were you absent? (Optional)"
                    )

                    OutlinedTextField(
                        value = reason,
                        onValueChange = { reason = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("e.g. Personal reasons, illness...")
                        },
                        minLines = 2,
                        shape = RoundedCornerShape(14.dp),
                        colors = forgeTextFieldColors()
                    )
                }

                // NOTES
                Column(
                    verticalArrangement = Arrangement.spacedBy(9.dp)
                ) {
                    FormSectionTitle(
                        "Additional notes",
                        "Anything else you want to remember? (Optional)"
                    )

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Add more details...")
                        },
                        minLines = 2,
                        shape = RoundedCornerShape(14.dp),
                        colors = forgeTextFieldColors()
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
                            val date = dateState.selectedDateMillis
                                ?: return@Button

                            onSave(
                                Absence(
                                    date = date,
                                    periodNumbersCsv = if (fullDay) {
                                        ""
                                    } else {
                                        selectedPeriods.sorted().joinToString(",")
                                    },
                                    reason = reason.trim(),
                                    notes = notes.trim()
                                )
                            )
                        },
                        enabled = dateState.selectedDateMillis != null &&
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
                            "Save Absence",
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }

    // DATE PICKER
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text(
                        "Confirm",
                        color = ForgeColors.Primary
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text(
                        "Cancel",
                        color = ForgeColors.TextSecondary
                    )
                }
            }
        ) {
            DatePicker(state = dateState)
        }
    }
}

@Composable
private fun FormSectionTitle(
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
private fun AbsenceTypeCard(
    title: String,
    subtitle: String,
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
        border = androidx.compose.foundation.BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) {
                ForgeColors.Primary
            } else {
                ForgeColors.TextSecondary.copy(alpha = 0.15f)
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(13.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = if (selected) "✓ $title" else title,
                color = if (selected) {
                    ForgeColors.Primary
                } else {
                    ForgeColors.TextPrimary
                },
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = subtitle,
                color = ForgeColors.TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun forgeTextFieldColors() =
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
