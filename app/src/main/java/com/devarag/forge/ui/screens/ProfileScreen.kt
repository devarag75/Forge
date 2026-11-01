
package com.devarag.forge.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devarag.forge.ui.design.ForgeColors

@Composable
fun ProfileScreen() {

    var name by remember { mutableStateOf("") }
    var course by remember { mutableStateOf("") }
    var college by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    var showEditDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    var notificationsEnabled by remember { mutableStateOf(true) }
    var hapticsEnabled by remember { mutableStateOf(true) }
    var dailyQuoteEnabled by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Background),
        contentPadding = PaddingValues(
            horizontal = 18.dp,
            vertical = 24.dp
        ),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        item {
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text(
                    "My Profile",
                    color = ForgeColors.TextPrimary,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Your personal space in Forge",
                    color = ForgeColors.TextSecondary
                )
            }
        }

        // PROFILE CARD
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = ForgeColors.Card
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        modifier = Modifier.size(92.dp),
                        shape = CircleShape,
                        color = ForgeColors.Primary.copy(alpha = 0.18f)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = name.firstOrNull()
                                    ?.uppercaseChar()?.toString() ?: "?",
                                color = ForgeColors.Primary,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Text(
                        text = name.ifBlank { "Your Name" },
                        color = ForgeColors.TextPrimary,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = course.ifBlank { "Add your course" },
                        color = ForgeColors.TextSecondary
                    )

                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = ForgeColors.Primary.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = year.ifBlank { "Academic Year" },
                            modifier = Modifier.padding(
                                horizontal = 16.dp,
                                vertical = 7.dp
                            ),
                            color = ForgeColors.Primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    OutlinedButton(
                        onClick = { showEditDialog = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = ForgeColors.TextPrimary
                        )
                    ) {
                        Text("Edit Profile")
                    }
                }
            }
        }

        // PERSONAL INFORMATION
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileSectionHeader(
                    "Personal Information",
                    "Manage your profile details"
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ForgeColors.Card
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp)
                    ) {
                        ProfileInfoRow(
                            "👤", "Full Name",
                            name.ifBlank { "Not set" }
                        )

                        HorizontalDivider(
                            color = ForgeColors.TextSecondary.copy(alpha = 0.12f)
                        )

                        ProfileInfoRow(
                            "🎓", "Course",
                            course.ifBlank { "Not set" }
                        )

                        HorizontalDivider(
                            color = ForgeColors.TextSecondary.copy(alpha = 0.12f)
                        )

                        ProfileInfoRow(
                            "🏫", "College",
                            college.ifBlank { "Not set" }
                        )

                        HorizontalDivider(
                            color = ForgeColors.TextSecondary.copy(alpha = 0.12f)
                        )

                        ProfileInfoRow(
                            "📚", "Academic Year",
                            year.ifBlank { "Not set" }
                        )
                    }
                }
            }
        }

        // GOALS
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileSectionHeader(
                    "My Goals",
                    "Your personal goals and achievements"
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
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("🎯", style = MaterialTheme.typography.headlineLarge)

                        Text(
                            "No goals added yet",
                            color = ForgeColors.TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            "Your goals and achievements will appear here.",
                            color = ForgeColors.TextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }

        // SETTINGS
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileSectionHeader(
                    "App Settings",
                    "Customise your Forge experience"
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ForgeColors.Card
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ProfileSettingRow(
                            "🔔",
                            "Notifications",
                            "Notification preference",
                            notificationsEnabled
                        ) {
                            notificationsEnabled = it
                        }

                        HorizontalDivider(
                            color = ForgeColors.TextSecondary.copy(alpha = 0.12f)
                        )

                        ProfileSettingRow(
                            "📳",
                            "Haptic Feedback",
                            "Vibration preference",
                            hapticsEnabled
                        ) {
                            hapticsEnabled = it
                        }

                        HorizontalDivider(
                            color = ForgeColors.TextSecondary.copy(alpha = 0.12f)
                        )

                        ProfileSettingRow(
                            "✨",
                            "Daily Motivation",
                            "Daily motivational content",
                            dailyQuoteEnabled
                        ) {
                            dailyQuoteEnabled = it
                        }
                    }
                }
            }
        }

        // ABOUT
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileSectionHeader(
                    "About",
                    "Information about the application"
                )

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showAboutDialog = true },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = ForgeColors.Card
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = RoundedCornerShape(14.dp),
                            color = ForgeColors.Primary.copy(alpha = 0.16f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    "F",
                                    color = ForgeColors.Primary,
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Forge",
                                color = ForgeColors.TextPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                "Your productivity companion",
                                color = ForgeColors.TextSecondary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        Text(
                            "›",
                            color = ForgeColors.TextSecondary,
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                }
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "MADE WITH PURPOSE",
                    color = ForgeColors.Primary,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(5.dp))

                Text(
                    "Forge • Your journey, your progress",
                    color = ForgeColors.TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    // EDIT PROFILE
    if (showEditDialog) {
        ProfileEditDialog(
            name,
            course,
            college,
            year,
            onDismiss = { showEditDialog = false },
            onSave = { newName, newCourse, newCollege, newYear ->
                name = newName
                course = newCourse
                college = newCollege
                year = newYear
                showEditDialog = false
            }
        )
    }

    // ABOUT DIALOG
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            containerColor = ForgeColors.Card,
            title = {
                Text(
                    "About Forge",
                    color = ForgeColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Forge is a personal productivity application.",
                        color = ForgeColors.TextPrimary
                    )
                    Text(
                        "Organise tasks, manage activities, take notes and track productivity.",
                        color = ForgeColors.TextSecondary
                    )
                    Text(
                        "Version 1.0",
                        color = ForgeColors.Primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { showAboutDialog = false }) {
                    Text("Close", color = ForgeColors.Primary)
                }
            }
        )
    }
}

@Composable
private fun ProfileSectionHeader(
    title: String,
    subtitle: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            title,
            color = ForgeColors.TextPrimary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            subtitle,
            color = ForgeColors.TextSecondary,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
private fun ProfileInfoRow(
    symbol: String,
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(symbol, style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                color = ForgeColors.TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                value,
                color = ForgeColors.TextPrimary,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun ProfileSettingRow(
    symbol: String,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(symbol, style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                title,
                color = ForgeColors.TextPrimary,
                fontWeight = FontWeight.Medium
            )
            Text(
                subtitle,
                color = ForgeColors.TextSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Composable
private fun ProfileEditDialog(
    initialName: String,
    initialCourse: String,
    initialCollege: String,
    initialYear: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var course by remember { mutableStateOf(initialCourse) }
    var college by remember { mutableStateOf(initialCollege) }
    var year by remember { mutableStateOf(initialYear) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = ForgeColors.Card,
        title = {
            Text(
                "Edit Profile",
                color = ForgeColors.TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ProfileEditField(name, { name = it }, "Full Name")
                ProfileEditField(course, { course = it }, "Course")
                ProfileEditField(college, { college = it }, "College")
                ProfileEditField(year, { year = it }, "Academic Year")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(
                        name.trim(),
                        course.trim(),
                        college.trim(),
                        year.trim()
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ForgeColors.Primary
                )
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = ForgeColors.TextSecondary)
            }
        }
    )
}

@Composable
private fun ProfileEditField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = ForgeColors.TextPrimary,
            unfocusedTextColor = ForgeColors.TextPrimary,
            focusedBorderColor = ForgeColors.Primary,
            unfocusedBorderColor = ForgeColors.TextSecondary.copy(alpha = 0.4f),
            focusedLabelColor = ForgeColors.Primary,
            unfocusedLabelColor = ForgeColors.TextSecondary,
            cursorColor = ForgeColors.Primary
        )
    )
}
