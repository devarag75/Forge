
package com.devarag.forge.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devarag.forge.ui.components.AddTaskDialog
import com.devarag.forge.ui.components.TaskItem
import com.devarag.forge.ui.design.ForgeColors
import com.devarag.forge.ui.viewmodel.NotesViewModel
import com.devarag.forge.ui.viewmodel.TaskViewModel
import com.devarag.forge.ui.viewmodel.TaskViewModelFactory

private val PurpleLight = Color(0xFFB879FF)
private val MutedText = Color(0xFF9695AD)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val context = LocalContext.current

    val taskViewModel: TaskViewModel = viewModel(
        factory = TaskViewModelFactory(
            context.applicationContext as android.app.Application
        )
    )

    val notesViewModel: NotesViewModel = viewModel()

    val tasks by taskViewModel.tasks.collectAsState()
    val notes by notesViewModel.notes.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var showNotesScreen by remember { mutableStateOf(false) }

    val completedTasks = tasks.count { it.completed }
    val pendingTasks = tasks.size - completedTasks

    val progress = if (tasks.isEmpty()) {
        0f
    } else {
        completedTasks.toFloat() / tasks.size.toFloat()
    }

    val importantNotes = remember(notes) {
        notes.sortedWith(
            compareByDescending<com.devarag.forge.data.entity.Note> {
                it.isPinned
            }.thenByDescending {
                it.updatedAt
            }
        ).take(2)
    }

    Scaffold(
        containerColor = ForgeColors.Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true },
                containerColor = ForgeColors.Primary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add task"
                )
            }
        }
    ) { innerPadding ->

        if (showDialog) {
            AddTaskDialog(
                onDismiss = { showDialog = false },
                onAddTask = { title ->
                    taskViewModel.addTask(title)
                    showDialog = false
                }
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(ForgeColors.Background)
                .padding(innerPadding),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 22.dp,
                bottom = 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {

            // HEADER
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "YOUR PERSONAL WORKSPACE",
                            color = PurpleLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.8.sp
                        )

                        Spacer(Modifier.height(7.dp))

                        Text(
                            text = "Good day!",
                            color = ForgeColors.TextPrimary,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Make today count.",
                            color = MutedText,
                            fontSize = 13.sp
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(ForgeColors.Surface)
                            .border(
                                1.dp,
                                ForgeColors.Primary.copy(alpha = 0.5f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "F",
                            color = PurpleLight,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // PRODUCTIVITY OVERVIEW
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ForgeColors.Surface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        ForgeColors.Primary.copy(alpha = 0.25f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF29203F),
                                        ForgeColors.Surface,
                                        ForgeColors.Surface
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "PRODUCTIVITY",
                                    color = PurpleLight,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.5.sp
                                )

                                Spacer(Modifier.height(5.dp))

                                Text(
                                    "Your daily progress",
                                    color = ForgeColors.TextPrimary,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Text(
                                "TODAY",
                                color = PurpleLight,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(
                                        ForgeColors.Primary.copy(alpha = 0.15f)
                                    )
                                    .padding(
                                        horizontal = 12.dp,
                                        vertical = 7.dp
                                    )
                            )
                        }

                        Spacer(Modifier.height(20.dp))

                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressDisplay(
                                progress = progress,
                                completed = completedTasks,
                                total = tasks.size
                            )
                        }

                        Spacer(Modifier.height(22.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(9.dp)
                        ) {
                            HomeStatCard(
                                modifier = Modifier.weight(1f),
                                symbol = "✓",
                                value = completedTasks.toString(),
                                label = "Completed",
                                accent = Color(0xFF42D6A0)
                            )

                            HomeStatCard(
                                modifier = Modifier.weight(1f),
                                symbol = "◷",
                                value = pendingTasks.toString(),
                                label = "Pending",
                                accent = PurpleLight
                            )

                            HomeStatCard(
                                modifier = Modifier.weight(1f),
                                symbol = "▤",
                                value = notes.size.toString(),
                                label = "Notes",
                                accent = Color(0xFFFFBD68)
                            )
                        }
                    }
                }
            }

            // TASK HEADING
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "My Tasks",
                            color = ForgeColors.TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Stay focused on what matters",
                            color = MutedText,
                            fontSize = 12.sp
                        )
                    }

                    TextButton(onClick = { showDialog = true }) {
                        Text(
                            "+ Add task",
                            color = PurpleLight,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // TASK LIST
            if (tasks.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = ForgeColors.Surface
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(22.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "✓",
                                color = PurpleLight,
                                fontSize = 34.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(8.dp))

                            Text(
                                "A fresh start.",
                                color = ForgeColors.TextPrimary,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                "Add your first task and make today productive.",
                                color = MutedText,
                                fontSize = 12.sp
                            )

                            Spacer(Modifier.height(12.dp))

                            Button(
                                onClick = { showDialog = true },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = ForgeColors.Primary
                                )
                            ) {
                                Text("Create a task")
                            }
                        }
                    }
                }
            } else {
                items(
                    items = tasks,
                    key = { it.id }
                ) { task ->
                    TaskItem(
                        title = task.title,
                        completed = task.completed,
                        onCheckedChange = { checked ->
                            taskViewModel.updateTask(
                                task.copy(completed = checked)
                            )
                        },
                        onDelete = {
                            taskViewModel.deleteTask(task)
                        }
                    )
                }
            }

            // IMPORTANT NOTES
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Important Notes",
                            color = ForgeColors.TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Keep important things close",
                            color = MutedText,
                            fontSize = 12.sp
                        )
                    }

                    TextButton(
                        onClick = { showNotesScreen = true }
                    ) {
                        Text("View all", color = PurpleLight)
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showNotesScreen = true },
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ForgeColors.Surface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        ForgeColors.Primary.copy(alpha = 0.2f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (importantNotes.isEmpty()) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    "▤",
                                    color = PurpleLight,
                                    fontSize = 25.sp
                                )

                                Spacer(Modifier.width(12.dp))

                                Column {
                                    Text(
                                        "No important notes yet",
                                        color = ForgeColors.TextPrimary,
                                        fontWeight = FontWeight.SemiBold
                                    )

                                    Text(
                                        "Save information you need to remember.",
                                        color = MutedText,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        } else {
                            importantNotes.forEach { note ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(ForgeColors.Background)
                                        .padding(13.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        "▤",
                                        color = PurpleLight,
                                        fontSize = 21.sp
                                    )

                                    Spacer(Modifier.width(12.dp))

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            note.title,
                                            color = ForgeColors.TextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Text(
                                            note.content.ifBlank {
                                                note.category
                                            },
                                            color = MutedText,
                                            fontSize = 11.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }

                                    if (note.isPinned) {
                                        Text(
                                            "PINNED",
                                            color = PurpleLight,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }

                        OutlinedButton(
                            onClick = { showNotesScreen = true },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(13.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = PurpleLight
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                ForgeColors.Primary.copy(alpha = 0.5f)
                            )
                        ) {
                            Text("Open Forge Notes")
                        }
                    }
                }
            }
        }
    }

    // EXISTING FULL-SCREEN NOTES
    if (showNotesScreen) {
        Dialog(
            onDismissRequest = {
                showNotesScreen = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = ForgeColors.Background
            ) {
                NotesScreen(
                    onClose = {
                        showNotesScreen = false
                    },
                    notesViewModel = notesViewModel
                )
            }
        }
    }
}

@Composable
private fun CircularProgressDisplay(
    progress: Float,
    completed: Int,
    total: Int
) {
    Box(
        modifier = Modifier.size(185.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 12.dp.toPx()

            drawArc(
                color = Color(0xFF39334C),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                )
            )

            drawArc(
                brush = Brush.sweepGradient(
                    listOf(
                        ForgeColors.Primary,
                        PurpleLight,
                        Color(0xFF8650E8),
                        ForgeColors.Primary
                    )
                ),
                startAngle = -90f,
                sweepAngle = 360f * progress.coerceIn(0f, 1f),
                useCenter = false,
                style = Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                )
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "${(progress * 100).toInt()}%",
                color = ForgeColors.TextPrimary,
                fontSize = 39.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                "TASK COMPLETION",
                color = MutedText,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )

            Spacer(Modifier.height(5.dp))

            Text(
                "$completed of $total tasks",
                color = PurpleLight,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun HomeStatCard(
    modifier: Modifier = Modifier,
    symbol: String,
    value: String,
    label: String,
    accent: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.045f))
            .border(
                1.dp,
                Color.White.copy(alpha = 0.07f),
                RoundedCornerShape(16.dp)
            )
            .padding(vertical = 13.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            symbol,
            color = accent,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(5.dp))

        Text(
            value,
            color = ForgeColors.TextPrimary,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            label,
            color = MutedText,
            fontSize = 10.sp
        )
    }
}
