package com.devarag.forge.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devarag.forge.data.entity.FocusSession
import com.devarag.forge.ui.viewmodel.InsightsViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@Composable
fun InsightsScreen(
    insightsViewModel: InsightsViewModel = viewModel()
) {
    val tasks by insightsViewModel.tasks.collectAsState()
    val sessions by insightsViewModel.focusSessions.collectAsState()

    val completedTasks = tasks.count { it.completed }
    val pendingTasks = tasks.count { !it.completed }
    val totalTasks = tasks.size

    val completionPercentage =
        if (totalTasks > 0) completedTasks * 100 / totalTasks else 0

    val totalFocusMinutes = sessions.sumOf { it.durationMinutes }

    val todayStart = remember {
        Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }

    val todayFocusMinutes = sessions
        .filter { it.completedAt >= todayStart }
        .sumOf { it.durationMinutes }

    val weeklyData = remember(sessions) {
        buildWeeklyFocusData(sessions)
    }

    val weeklyFocusMinutes = weeklyData.sumOf { it.minutes }

    val primaryColor = MaterialTheme.colorScheme.primary

    val hours = totalFocusMinutes / 60
    val minutes = totalFocusMinutes % 60

    ProvideTextStyle(
        value = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Text(
                    text = "Insights",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Your productivity at a glance",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Task overview
                Text(
                    text = "Task Overview",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InsightCard(
                        title = "Completed",
                        value = completedTasks.toString(),
                        subtitle = "Tasks finished",
                        modifier = Modifier.weight(1f)
                    )

                    InsightCard(
                        title = "Pending",
                        value = pendingTasks.toString(),
                        subtitle = "Tasks remaining",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Completion rate
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Completion Rate",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "$completionPercentage%",
                                style = MaterialTheme.typography.titleMedium,
                                color = primaryColor,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        LinearProgressIndicator(
                            progress = { completionPercentage / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(9.dp),
                            color = primaryColor,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Text(
                            text = "$completedTasks of $totalTasks tasks completed",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Focus overview
                Text(
                    text = "Focus Overview",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    InsightCard(
                        title = "Total Focus",
                        value = "${hours}h ${minutes}m",
                        subtitle = "All recorded sessions",
                        modifier = Modifier.weight(1f)
                    )

                    InsightCard(
                        title = "Today",
                        value = formatMinutes(todayFocusMinutes),
                        subtitle = "Today's focus time",
                        modifier = Modifier.weight(1f)
                    )
                }

                // Weekly chart
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Column {
                            Text(
                                text = "Weekly Focus",
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = "${formatMinutes(weeklyFocusMinutes)} focused in the last 7 days",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        WeeklyFocusChart(
                            data = weeklyData,
                            barColor = primaryColor
                        )
                    }
                }

                // Productivity summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Productivity Summary",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        SummaryRow(
                            label = "Total tasks",
                            value = totalTasks.toString()
                        )

                        SummaryRow(
                            label = "Completed tasks",
                            value = completedTasks.toString()
                        )

                        SummaryRow(
                            label = "Pending tasks",
                            value = pendingTasks.toString()
                        )

                        SummaryRow(
                            label = "Focus sessions",
                            value = sessions.size.toString()
                        )

                        SummaryRow(
                            label = "Weekly focus",
                            value = formatMinutes(weeklyFocusMinutes)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InsightCard(
    title: String,
    value: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private data class DailyFocus(
    val label: String,
    val minutes: Int
)

private fun buildWeeklyFocusData(
    sessions: List<FocusSession>
): List<DailyFocus> {
    val calendar = Calendar.getInstance()

    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    calendar.add(Calendar.DAY_OF_YEAR, -6)

    val dateFormat = SimpleDateFormat("EEE", Locale.getDefault())

    return (0 until 7).map {
        val start = calendar.timeInMillis
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val end = calendar.timeInMillis

        val minutes = sessions
            .filter { session ->
                session.completedAt >= start &&
                        session.completedAt < end
            }
            .sumOf { session -> session.durationMinutes }

        val label = dateFormat.format(Date(start))

        DailyFocus(label, minutes)
    }
}

@Composable
private fun WeeklyFocusChart(
    data: List<DailyFocus>,
    barColor: Color
) {
    val maxMinutes = (data.maxOfOrNull { it.minutes } ?: 0).coerceAtLeast(60)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        data.forEach { day ->
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (day.minutes > 0) "${day.minutes}m" else "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )

                Spacer(Modifier.height(6.dp))

                Canvas(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    val barWidth = size.width * 0.48f
                    val barHeight =
                        (day.minutes.toFloat() / maxMinutes) * size.height

                    drawRoundRect(
                        color = barColor.copy(alpha = 0.18f),
                        topLeft = Offset(
                            (size.width - barWidth) / 2,
                            0f
                        ),
                        size = Size(barWidth, size.height),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                            12f,
                            12f
                        )
                    )

                    if (day.minutes > 0) {
                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(
                                (size.width - barWidth) / 2,
                                size.height - barHeight
                            ),
                            size = Size(barWidth, barHeight),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                                12f,
                                12f
                            )
                        )
                    }
                }

                Spacer(Modifier.height(8.dp))

                Text(
                    text = day.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun formatMinutes(minutes: Int): String {
    val hours = minutes / 60
    val remainingMinutes = minutes % 60

    return when {
        hours > 0 && remainingMinutes > 0 -> "${hours}h ${remainingMinutes}m"
        hours > 0 -> "${hours}h"
        else -> "${remainingMinutes}m"
    }
}