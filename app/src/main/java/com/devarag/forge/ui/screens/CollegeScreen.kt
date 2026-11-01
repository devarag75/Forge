
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devarag.forge.data.entity.Absence
import com.devarag.forge.data.entity.DutyLeave
import com.devarag.forge.ui.components.AddAbsenceDialog
import com.devarag.forge.ui.components.AddDutyLeaveDialog
import com.devarag.forge.ui.design.ForgeColors
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Background),
        contentPadding = PaddingValues(
            start = 18.dp,
            end = 18.dp,
            top = 20.dp,
            bottom = 120.dp
        ),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {

        // HEADER
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = "College",
                    color = ForgeColors.TextPrimary,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Manage your attendance and duty leaves",
                    color = ForgeColors.TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // SUMMARY CARDS
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CollegeStatCard(
                    title = "Absences",
                    count = absences.size,
                    subtitle = "Recorded",
                    accent = ForgeColors.Error,
                    modifier = Modifier.weight(1f)
                )

                CollegeStatCard(
                    title = "Duty Leaves",
                    count = dutyLeaves.size,
                    subtitle = "Recorded",
                    accent = ForgeColors.Primary,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // QUICK ACTIONS
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Quick Actions",
                    color = ForgeColors.TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { showAbsenceDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForgeColors.Primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null
                        )
                        Spacer(Modifier.width(5.dp))
                        Text("Mark Absent")
                    }

                    OutlinedButton(
                        onClick = { showDutyDialog = true },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = ForgeColors.TextPrimary
                        )
                    ) {
                        Text("Duty Leave")
                    }
                }
            }
        }

        // ABSENCE HISTORY HEADER
        item {
            HistorySectionHeader(
                title = "Absence History",
                count = absences.size
            )
        }

        if (absences.isEmpty()) {
            item {
                CollegeEmptyState(
                    title = "No absences yet",
                    description = "Your absence records will appear here."
                )
            }
        } else {
            items(
                items = absences,
                key = { "absence_${it.id}" }
            ) { absence ->
                CollegeHistoryCard(
                    title = formatDate(absence.date),
                    subtitle = if (absence.periodNumbersCsv.isBlank()) {
                        "Full day"
                    } else {
                        "Periods: ${absence.periodNumbersCsv}"
                    },
                    details = listOf(absence.reason, absence.notes)
                        .filter { it.isNotBlank() },
                    accent = ForgeColors.Error,
                    onDelete = {
                        absenceToDelete = absence
                    }
                )
            }
        }

        // DUTY LEAVE HISTORY HEADER
        item {
            HistorySectionHeader(
                title = "Duty Leave History",
                count = dutyLeaves.size
            )
        }

        if (dutyLeaves.isEmpty()) {
            item {
                CollegeEmptyState(
                    title = "No duty leaves yet",
                    description = "Your approved and pending duty leaves will appear here."
                )
            }
        } else {
            items(
                items = dutyLeaves,
                key = { "duty_${it.id}" }
            ) { leave ->
                CollegeHistoryCard(
                    title = leave.eventName,
                    subtitle = "${formatDate(leave.startDate)} – ${formatDate(leave.endDate)}",
                    details = buildList {
                        add("Status: ${leave.approvalStatus}")
                        if (leave.hoursCoveredCsv.isNotBlank()) {
                            add("Periods: ${leave.hoursCoveredCsv}")
                        }
                        if (leave.reason.isNotBlank()) {
                            add(leave.reason)
                        }
                        if (leave.notes.isNotBlank()) {
                            add(leave.notes)
                        }
                    },
                    accent = ForgeColors.Primary,
                    onDelete = {
                        dutyLeaveToDelete = leave
                    }
                )
            }
        }

        // BACKUP SECTION
        item {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Backup & Export",
                    color = ForgeColors.TextPrimary,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Keep your college records safe or export them.",
                    color = ForgeColors.TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ForgeColors.Card
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Export your records",
                            color = ForgeColors.TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )

                        OutlinedButton(
                            onClick = {
                                try {
                                    exportCollegeImage(
                                        context,
                                        absences,
                                        dutyLeaves
                                    )
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
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = ForgeColors.TextPrimary
                            )
                        ) {
                            Text("Export History as Image")
                        }

                        Divider(color = ForgeColors.TextSecondary.copy(alpha = 0.15f))

                        Text(
                            text = "JSON Backup",
                            color = ForgeColors.TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Button(
                            onClick = {
                                backupLauncher.launch("Forge_College_Backup.json")
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForgeColors.Primary
                            )
                        ) {
                            Text("Create JSON Backup")
                        }

                        OutlinedButton(
                            onClick = {
                                restoreLauncher.launch(
                                    arrayOf(
                                        "application/json",
                                        "text/json",
                                        "*/*"
                                    )
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = ForgeColors.TextPrimary
                            )
                        ) {
                            Text("Restore JSON Backup")
                        }
                    }
                }
            }
        }
    }

    // ABSENCE DELETE DIALOG
    absenceToDelete?.let { absence ->
        AlertDialog(
            onDismissRequest = {
                absenceToDelete = null
            },
            containerColor = ForgeColors.Card,
            title = {
                Text(
                    "Delete absence record?",
                    color = ForgeColors.TextPrimary
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete the absence record for ${formatDate(absence.date)}? This action cannot be undone.",
                    color = ForgeColors.TextSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        collegeViewModel.deleteAbsence(absence)
                        absenceToDelete = null
                    }
                ) {
                    Text("Delete", color = ForgeColors.Error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        absenceToDelete = null
                    }
                ) {
                    Text("Cancel", color = ForgeColors.TextSecondary)
                }
            }
        )
    }

    // DUTY LEAVE DELETE DIALOG
    dutyLeaveToDelete?.let { leave ->
        AlertDialog(
            onDismissRequest = {
                dutyLeaveToDelete = null
            },
            containerColor = ForgeColors.Card,
            title = {
                Text(
                    "Delete duty leave?",
                    color = ForgeColors.TextPrimary
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${leave.eventName}\"? This action cannot be undone.",
                    color = ForgeColors.TextSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        collegeViewModel.deleteDutyLeave(leave)
                        dutyLeaveToDelete = null
                    }
                ) {
                    Text("Delete", color = ForgeColors.Error)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dutyLeaveToDelete = null
                    }
                ) {
                    Text("Cancel", color = ForgeColors.TextSecondary)
                }
            }
        )
    }

    // ADD ABSENCE DIALOG
    if (showAbsenceDialog) {
        AddAbsenceDialog(
            onDismiss = { showAbsenceDialog = false },
            onSave = { absence ->
                collegeViewModel.addAbsence(absence)
                showAbsenceDialog = false
            }
        )
    }

    // ADD DUTY LEAVE DIALOG
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
    subtitle: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = ForgeColors.Card
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(
                text = title,
                color = ForgeColors.TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = count.toString(),
                color = accent,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = subtitle,
                color = ForgeColors.TextSecondary,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun HistorySectionHeader(
    title: String,
    count: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            color = ForgeColors.TextPrimary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Surface(
            shape = RoundedCornerShape(50),
            color = ForgeColors.Primary.copy(alpha = 0.16f)
        ) {
            Text(
                text = count.toString(),
                modifier = Modifier.padding(
                    horizontal = 12.dp,
                    vertical = 5.dp
                ),
                color = ForgeColors.Primary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CollegeHistoryCard(
    title: String,
    subtitle: String,
    details: List<String>,
    accent: Color,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = ForgeColors.Card
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(62.dp)
                    .background(
                        accent,
                        RoundedCornerShape(50)
                    )
            )

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(
                    text = title,
                    color = ForgeColors.TextPrimary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = subtitle,
                    color = ForgeColors.TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )

                details.forEach { detail ->
                    Text(
                        text = detail,
                        color = if (detail.startsWith("Status:")) {
                            accent
                        } else {
                            ForgeColors.TextSecondary
                        },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            TextButton(
                onClick = onDelete,
                contentPadding = PaddingValues(
                    horizontal = 8.dp,
                    vertical = 4.dp
                )
            ) {
                Text(
                    text = "Delete",
                    color = ForgeColors.Error,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun CollegeEmptyState(
    title: String,
    description: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(17.dp),
        colors = CardDefaults.cardColors(
            containerColor = ForgeColors.Card
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Text(
                text = title,
                color = ForgeColors.TextPrimary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Text(
                text = description,
                color = ForgeColors.TextSecondary,
                style = MaterialTheme.typography.bodySmall
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
