
package com.devarag.forge.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devarag.forge.ui.design.ForgeColors
import com.devarag.forge.ui.viewmodel.FocusViewModel

private val FocusPurpleLight = Color(0xFFBE83FF)
private val FocusMuted = Color(0xFF9695AD)

@Composable
fun FocusScreen() {

    val viewModel: FocusViewModel = viewModel()

    var hoursInput by remember {
        mutableStateOf(viewModel.selectedHours.toString())
    }

    var minutesInput by remember {
        mutableStateOf(viewModel.selectedMinutes.toString())
    }

    var inputError by remember {
        mutableStateOf(false)
    }

    val isRunning = viewModel.isRunning
    val remainingSeconds = viewModel.remainingSeconds
    val selectedDuration = viewModel.selectedDurationSeconds

    val progress = if (selectedDuration > 0) {
        remainingSeconds.toFloat() / selectedDuration.toFloat()
    } else {
        0f
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Background)
            .systemBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // HEADER
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "STAY IN THE ZONE",
                    color = FocusPurpleLight,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.8.sp
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "Focus Timer",
                    color = ForgeColors.TextPrimary,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Set your own focus duration.",
                    color = FocusMuted,
                    fontSize = 12.sp
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        ForgeColors.Primary.copy(alpha = 0.15f)
                    )
                    .border(
                        1.dp,
                        ForgeColors.Primary.copy(alpha = 0.35f),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 9.dp)
            ) {
                Text(
                    text = if (isRunning) "RUNNING" else "CUSTOM",
                    color = FocusPurpleLight,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(Modifier.height(25.dp))

        // TIMER CARD
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
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
                                Color(0xFF28203F),
                                ForgeColors.Surface,
                                ForgeColors.Surface
                            )
                        )
                    )
                    .padding(horizontal = 18.dp, vertical = 25.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "YOUR FOCUS SESSION",
                    color = FocusMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(Modifier.height(20.dp))

                // CIRCULAR TIMER
                Box(
                    modifier = Modifier.size(235.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {

                        val strokeWidth = 13.dp.toPx()

                        drawArc(
                            color = Color(0xFF39334C),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(
                                width = strokeWidth,
                                cap = StrokeCap.Round
                            )
                        )

                        drawArc(
                            brush = Brush.sweepGradient(
                                listOf(
                                    ForgeColors.Primary,
                                    FocusPurpleLight,
                                    Color(0xFF8650E8),
                                    ForgeColors.Primary
                                )
                            ),
                            startAngle = -90f,
                            sweepAngle = 360f * progress.coerceIn(0f, 1f),
                            useCenter = false,
                            style = Stroke(
                                width = strokeWidth,
                                cap = StrokeCap.Round
                            )
                        )
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = String.format(
                                "%02d:%02d:%02d",
                                remainingSeconds / 3600,
                                (remainingSeconds % 3600) / 60,
                                remainingSeconds % 60
                            ),
                            color = ForgeColors.TextPrimary,
                            fontSize = 34.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(7.dp))

                        Text(
                            text = if (isRunning) {
                                "STAY FOCUSED"
                            } else if (remainingSeconds == 0) {
                                "SESSION COMPLETED"
                            } else {
                                "READY TO FOCUS"
                            },
                            color = FocusPurpleLight,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(Modifier.height(15.dp))

                Text(
                    text = if (isRunning) {
                        "Keep going. You're making progress."
                    } else if (remainingSeconds == 0) {
                        "Great work! Your timer has finished."
                    } else {
                        "One task at a time. No distractions."
                    },
                    color = FocusMuted,
                    fontSize = 12.sp
                )

                Spacer(Modifier.height(24.dp))

                // START / PAUSE
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            if (isRunning) {
                                viewModel.pause()
                            } else {
                                viewModel.start()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ForgeColors.Primary
                        ),
                        enabled = remainingSeconds > 0
                    ) {
                        Text(
                            text = if (isRunning) "Ⅱ Pause" else "▶ Start",
                            fontWeight = FontWeight.Bold
                        )
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.reset()
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(15.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = FocusPurpleLight
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            ForgeColors.Primary.copy(alpha = 0.5f)
                        )
                    ) {
                        Text("↻ Reset")
                    }
                }
            }
        }

        Spacer(Modifier.height(25.dp))

        // CUSTOM DURATION
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = ForgeColors.Surface
            ),
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                Color.White.copy(alpha = 0.06f)
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp)
            ) {
                Text(
                    text = "Set Focus Duration",
                    color = ForgeColors.TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "Choose how long you want to focus.",
                    color = FocusMuted,
                    fontSize = 12.sp
                )

                Spacer(Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Hours",
                            color = FocusMuted,
                            fontSize = 12.sp
                        )

                        Spacer(Modifier.height(6.dp))

                        OutlinedTextField(
                            value = hoursInput,
                            onValueChange = {
                                hoursInput = it.filter(Char::isDigit).take(2)
                                inputError = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isRunning,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            placeholder = { Text("0") },
                            shape = RoundedCornerShape(13.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = ForgeColors.TextPrimary,
                                unfocusedTextColor = ForgeColors.TextPrimary,
                                focusedBorderColor = ForgeColors.Primary,
                                unfocusedBorderColor = Color(0xFF454052),
                                disabledTextColor = FocusMuted
                            )
                        )
                    }

                    Text(
                        ":",
                        color = FocusPurpleLight,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Minutes",
                            color = FocusMuted,
                            fontSize = 12.sp
                        )

                        Spacer(Modifier.height(6.dp))

                        OutlinedTextField(
                            value = minutesInput,
                            onValueChange = {
                                minutesInput = it.filter(Char::isDigit).take(2)
                                inputError = false
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isRunning,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            placeholder = { Text("25") },
                            shape = RoundedCornerShape(13.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = ForgeColors.TextPrimary,
                                unfocusedTextColor = ForgeColors.TextPrimary,
                                focusedBorderColor = ForgeColors.Primary,
                                unfocusedBorderColor = Color(0xFF454052),
                                disabledTextColor = FocusMuted
                            )
                        )
                    }
                }

                if (inputError) {
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Enter a valid duration. Hours: 0–23, minutes: 0–59. Total must be greater than zero.",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 11.sp
                    )
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        val hours = hoursInput.toIntOrNull()
                        val minutes = minutesInput.toIntOrNull()

                        if (
                            hours == null ||
                            minutes == null ||
                            !viewModel.setDuration(hours, minutes)
                        ) {
                            inputError = true
                        } else {
                            inputError = false
                        }
                    },
                    enabled = !isRunning,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(49.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ForgeColors.Primary
                    )
                ) {
                    Text(
                        text = "Apply Duration",
                        fontWeight = FontWeight.Bold
                    )
                }

                if (isRunning) {
                    Spacer(Modifier.height(8.dp))

                    Text(
                        text = "Pause the timer before changing its duration.",
                        color = FocusMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(25.dp))

        // SIMPLE FOCUS TIP
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = ForgeColors.Surface
            )
        ) {
            Column(
                modifier = Modifier.padding(17.dp)
            ) {
                Text(
                    text = "A little reminder",
                    color = ForgeColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    text = "Put distractions aside. Focus on one thing at a time.",
                    color = FocusMuted,
                    fontSize = 12.sp
                )
            }
        }

        Spacer(Modifier.height(25.dp))
    }
}
