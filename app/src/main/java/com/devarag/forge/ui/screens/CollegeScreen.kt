package com.devarag.forge.ui.screens

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devarag.forge.ui.components.AddAbsenceDialog
import com.devarag.forge.ui.components.AddDutyLeaveDialog
import com.devarag.forge.data.entity.Absence
import com.devarag.forge.data.entity.DutyLeave
import com.devarag.forge.ui.viewmodel.CollegeViewModel
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun CollegeScreen(
    collegeViewModel: CollegeViewModel = viewModel()
) {
    val context = LocalContext.current

    val absences by collegeViewModel.absences.collectAsState()
    val dutyLeaves by collegeViewModel.dutyLeaves.collectAsState()

    var showAbsenceDialog by remember { mutableStateOf(false) }
    var showDutyDialog by remember { mutableStateOf(false) }

    var absenceToDelete by remember { mutableStateOf<Absence?>(null) }
    var dutyLeaveToDelete by remember { mutableStateOf<DutyLeave?>(null) }

    val backupLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            try {
                val json = createBackupJson(absences, dutyLeaves)
                context.contentResolver.openOutputStream(it)?.use { output ->
                    output.write(json.toByteArray(Charsets.UTF_8))
                }
                Toast.makeText(
                    context,
                    "Backup saved successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Backup failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    val restoreLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            try {
                val json = context.contentResolver.openInputStream(it)
                    ?.bufferedReader()
                    ?.use { reader -> reader.readText() }
                    ?: throw Exception("Unable to read file")

                val (restoredAbsences, restoredDutyLeaves) =
                    parseBackupJson(json)

                collegeViewModel.restoreBackup(
                    restoredAbsences,
                    restoredDutyLeaves
                )

                Toast.makeText(
                    context,
                    "Backup restored successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                Toast.makeText(
                    context,
                    "Restore failed: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    ProvideTextStyle(
        value = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface
        )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "College",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Text(
                        text = "Your attendance records",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CollegeStatCard(
                        title = "Absences",
                        count = absences.size,
                        modifier = Modifier.weight(1f)
                    )

                    CollegeStatCard(
                        title = "Duty Leaves",
                        count = dutyLeaves.size,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { showAbsenceDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add absence"
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("Mark Absent")
                    }

                    OutlinedButton(
                        onClick = { showDutyDialog = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Duty Leave")
                    }
                }
            }

            item {
                Text(
                    text = "Absence History",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (absences.isEmpty()) {
                item {
                    Text(
                        text = "No absence records yet.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(absences, key = { it.id }) { absence ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = formatDate(absence.date),
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = if (absence.periodNumbersCsv.isBlank()) {
                                        "Full day"
                                    } else {
                                        "Periods: ${absence.periodNumbersCsv}"
                                    },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (absence.reason.isNotBlank()) {
                                    Text(
                                        text = absence.reason,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                if (absence.notes.isNotBlank()) {
                                    Text(
                                        text = absence.notes,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            TextButton(
                                onClick = {
                                    absenceToDelete = absence
                                }
                            ) {
                                Text(
                                    "Delete",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Duty Leave History",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (dutyLeaves.isEmpty()) {
                item {
                    Text(
                        text = "No duty leave records yet.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                items(dutyLeaves, key = { it.id }) { leave ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = leave.eventName,
                                    style = MaterialTheme.typography.titleMedium
                                )

                                Text(
                                    text = "${formatDate(leave.startDate)} – " +
                                            formatDate(leave.endDate),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Text(
                                    text = "Status: ${leave.approvalStatus}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                if (leave.hoursCoveredCsv.isNotBlank()) {
                                    Text(
                                        text = "Periods: ${leave.hoursCoveredCsv}",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }

                                if (leave.reason.isNotBlank()) {
                                    Text(
                                        text = leave.reason,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            TextButton(
                                onClick = {
                                    dutyLeaveToDelete = leave
                                }
                            ) {
                                Text(
                                    "Delete",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }

            item {
                Text(
                    text = "Export & Backup",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                OutlinedButton(
                    onClick = {
                        try {
                            exportCollegeImage(context, absences, dutyLeaves)
                            Toast.makeText(
                                context,
                                "College report saved to Pictures/Forge",
                                Toast.LENGTH_LONG
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Image export failed: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Export History as Image")
                }

                Spacer(Modifier.height(8.dp))

                Button(
                    onClick = {
                        backupLauncher.launch("Forge_College_Backup.json")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create JSON Backup")
                }

                Spacer(Modifier.height(8.dp))

                OutlinedButton(
                    onClick = {
                        restoreLauncher.launch(
                            arrayOf("application/json", "text/json", "*/*")
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Restore JSON Backup")
                }
            }
        }
    }

    // Confirm absence deletion
    absenceToDelete?.let { absence ->
        AlertDialog(
            onDismissRequest = {
                absenceToDelete = null
            },
            title = {
                Text(
                    "Delete absence record?",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete the absence record " +
                            "for ${formatDate(absence.date)}? " +
                            "This action cannot be undone.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        collegeViewModel.deleteAbsence(absence)
                        absenceToDelete = null
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
                        absenceToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // Confirm duty leave deletion
    dutyLeaveToDelete?.let { leave ->
        AlertDialog(
            onDismissRequest = {
                dutyLeaveToDelete = null
            },
            title = {
                Text(
                    "Delete duty leave?",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${leave.eventName}\"? " +
                            "This action cannot be undone.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        collegeViewModel.deleteDutyLeave(leave)
                        dutyLeaveToDelete = null
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
                        dutyLeaveToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showAbsenceDialog) {
        AddAbsenceDialog(
            onDismiss = { showAbsenceDialog = false },
            onSave = { absence ->
                collegeViewModel.addAbsence(absence)
                showAbsenceDialog = false
            }
        )
    }

    if (showDutyDialog) {
        AddDutyLeaveDialog(
            onDismiss = { showDutyDialog = false },
            onSave = { dutyLeave ->
                collegeViewModel.addDutyLeave(dutyLeave)
                showDutyDialog = false
            }
        )
    }
}

@Composable
private fun CollegeStatCard(
    title: String,
    count: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = count.toString(),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun formatDate(timestamp: Long): String {
    return SimpleDateFormat(
        "dd MMM yyyy",
        Locale.getDefault()
    ).format(Date(timestamp))
}

// JSON BACKUP

private fun createBackupJson(
    absences: List<Absence>,
    dutyLeaves: List<DutyLeave>
): String {
    val root = JSONObject()
    root.put("formatVersion", 1)
    root.put("backupType", "Forge College Backup")
    root.put("createdAt", System.currentTimeMillis())

    val absenceArray = JSONArray()

    absences.forEach { absence ->
        absenceArray.put(
            JSONObject().apply {
                put("id", absence.id)
                put("date", absence.date)
                put("periodNumbersCsv", absence.periodNumbersCsv)
                put("reason", absence.reason)
                put("notes", absence.notes)
                put("createdAt", absence.createdAt)
            }
        )
    }

    val dutyArray = JSONArray()

    dutyLeaves.forEach { leave ->
        dutyArray.put(
            JSONObject().apply {
                put("id", leave.id)
                put("eventName", leave.eventName)
                put("startDate", leave.startDate)
                put("endDate", leave.endDate)
                put("hoursCoveredCsv", leave.hoursCoveredCsv)
                put("reason", leave.reason)
                put("approvalStatus", leave.approvalStatus)
                put("notes", leave.notes)
                put("createdAt", leave.createdAt)
            }
        )
    }

    root.put("absences", absenceArray)
    root.put("dutyLeaves", dutyArray)

    return root.toString(4)
}

private fun parseBackupJson(
    json: String
): Pair<List<Absence>, List<DutyLeave>> {
    val root = JSONObject(json)

    require(root.optString("backupType") == "Forge College Backup") {
        "This is not a Forge College backup file"
    }

    val absenceList = mutableListOf<Absence>()
    val dutyLeaveList = mutableListOf<DutyLeave>()

    val absenceArray = root.getJSONArray("absences")

    for (i in 0 until absenceArray.length()) {
        val item = absenceArray.getJSONObject(i)

        absenceList.add(
            Absence(
                id = item.getInt("id"),
                date = item.getLong("date"),
                periodNumbersCsv = item.optString("periodNumbersCsv", ""),
                reason = item.optString("reason", ""),
                notes = item.optString("notes", ""),
                createdAt = item.optLong(
                    "createdAt",
                    System.currentTimeMillis()
                )
            )
        )
    }

    val dutyArray = root.getJSONArray("dutyLeaves")

    for (i in 0 until dutyArray.length()) {
        val item = dutyArray.getJSONObject(i)

        dutyLeaveList.add(
            DutyLeave(
                id = item.getInt("id"),
                eventName = item.getString("eventName"),
                startDate = item.getLong("startDate"),
                endDate = item.getLong("endDate"),
                hoursCoveredCsv = item.optString("hoursCoveredCsv", ""),
                reason = item.optString("reason", ""),
                approvalStatus = item.optString(
                    "approvalStatus",
                    "PENDING"
                ),
                notes = item.optString("notes", ""),
                createdAt = item.optLong(
                    "createdAt",
                    System.currentTimeMillis()
                )
            )
        )
    }

    return Pair(absenceList, dutyLeaveList)
}

// IMAGE EXPORT

private fun exportCollegeImage(
    context: Context,
    absences: List<Absence>,
    dutyLeaves: List<DutyLeave>
) {
    val width = 1200
    val rowHeight = 100
    val headerHeight = 380
    val footerHeight = 120
    val totalRows = absences.size + dutyLeaves.size
    val height = (headerHeight + totalRows * rowHeight + footerHeight)
        .coerceAtLeast(700)

    val bitmap = Bitmap.createBitmap(
        width,
        height,
        Bitmap.Config.ARGB_8888
    )

    val canvas = Canvas(bitmap)
    canvas.drawColor(android.graphics.Color.rgb(12, 16, 30))

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    fun drawText(
        text: String,
        x: Float,
        y: Float,
        size: Float,
        color: Int,
        bold: Boolean = false
    ) {
        paint.color = color
        paint.textSize = size
        paint.typeface = Typeface.create(
            "sans-serif",
            if (bold) Typeface.BOLD else Typeface.NORMAL
        )
        canvas.drawText(text, x, y, paint)
    }

    val white = android.graphics.Color.WHITE
    val muted = android.graphics.Color.rgb(170, 180, 200)
    val purple = android.graphics.Color.rgb(160, 100, 255)
    val line = android.graphics.Color.rgb(48, 56, 78)

    drawText("FORGE", 70f, 95f, 42f, purple, true)
    drawText("COLLEGE RECORD", 70f, 165f, 58f, white, true)
    drawText(
        "Absence and Duty Leave History",
        70f,
        220f,
        30f,
        muted
    )

    drawText(
        "TOTAL ABSENCES: ${absences.size}",
        70f,
        300f,
        27f,
        white,
        true
    )

    drawText(
        "TOTAL DUTY LEAVES: ${dutyLeaves.size}",
        600f,
        300f,
        27f,
        white,
        true
    )

    var y = 420f

    drawText("ABSENCE HISTORY", 70f, y, 32f, purple, true)
    y += 65f

    if (absences.isEmpty()) {
        drawText("No absence records", 70f, y, 25f, muted)
        y += rowHeight
    } else {
        absences.forEach { absence ->
            drawText(
                formatDate(absence.date),
                70f,
                y,
                26f,
                white,
                true
            )

            val periods = if (absence.periodNumbersCsv.isBlank()) {
                "Full day"
            } else {
                "Periods: ${absence.periodNumbersCsv}"
            }

            drawText(periods, 440f, y, 24f, muted)

            if (absence.reason.isNotBlank()) {
                drawText(
                    absence.reason.take(45),
                    800f,
                    y,
                    21f,
                    muted
                )
            }

            paint.color = line
            canvas.drawLine(70f, y + 25f, 1130f, y + 25f, paint)
            y += rowHeight
        }
    }

    y += 30f
    drawText("DUTY LEAVE HISTORY", 70f, y, 32f, purple, true)
    y += 65f

    if (dutyLeaves.isEmpty()) {
        drawText("No duty leave records", 70f, y, 25f, muted)
        y += rowHeight
    } else {
        dutyLeaves.forEach { leave ->
            drawText(
                leave.eventName.take(35),
                70f,
                y,
                26f,
                white,
                true
            )

            drawText(
                "${formatDate(leave.startDate)} - ${formatDate(leave.endDate)}",
                500f,
                y,
                22f,
                muted
            )

            drawText(
                leave.approvalStatus,
                900f,
                y,
                22f,
                purple,
                true
            )

            paint.color = line
            canvas.drawLine(70f, y + 25f, 1130f, y + 25f, paint)
            y += rowHeight
        }
    }

    drawText(
        "Generated by Forge • ${formatDate(System.currentTimeMillis())}",
        70f,
        (height - 45).toFloat(),
        21f,
        muted
    )

    val values = ContentValues().apply {
        put(
            MediaStore.Images.Media.DISPLAY_NAME,
            "Forge_College_${System.currentTimeMillis()}.png"
        )
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        put(
            MediaStore.Images.Media.RELATIVE_PATH,
            "${Environment.DIRECTORY_PICTURES}/Forge"
        )
        put(MediaStore.Images.Media.IS_PENDING, 1)
    }

    val uri: Uri = context.contentResolver.insert(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        values
    ) ?: throw Exception("Could not create image file")

    try {
        context.contentResolver.openOutputStream(uri)?.use { output ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)
        } ?: throw Exception("Could not save image")

        val completedValues = ContentValues().apply {
            put(MediaStore.Images.Media.IS_PENDING, 0)
        }

        context.contentResolver.update(uri, completedValues, null, null)
    } catch (e: Exception) {
        context.contentResolver.delete(uri, null, null)
        throw e
    } finally {
        bitmap.recycle()
    }

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/png"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(
        Intent.createChooser(shareIntent, "Share College Report")
    )
}