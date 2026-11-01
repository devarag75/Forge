
@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.devarag.forge.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devarag.forge.data.entity.Note
import com.devarag.forge.ui.design.ForgeColors
import com.devarag.forge.ui.viewmodel.NotesViewModel

private val NotePurple = Color(0xFFB879FF)
private val NoteMuted = Color(0xFF9695AD)
private val NoteCard = Color(0xFF171725)
private val NoteBorder = Color(0xFF343047)

private val noteCategories = listOf(
    "General",
    "College",
    "Academics",
    "Websites",
    "Projects",
    "Personal"
)

@Composable
fun NotesScreen(
    onClose: () -> Unit,
    notesViewModel: NotesViewModel
) {
    val notes by notesViewModel.notes.collectAsState()
    val query by notesViewModel.query.collectAsState()

    var showEditor by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    val pinnedCount = notes.count { it.isPinned }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ForgeColors.Background)
            .padding(horizontal = 20.dp)
    ) {

        Spacer(Modifier.height(12.dp))

        // HEADER
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    "YOUR PERSONAL SPACE",
                    color = NotePurple,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.8.sp
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    "Forge Notes",
                    color = ForgeColors.TextPrimary,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "Your personal knowledge space",
                    color = NoteMuted,
                    fontSize = 12.sp
                )
            }

            OutlinedButton(
                onClick = onClose,
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(
                    horizontal = 14.dp,
                    vertical = 8.dp
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = NotePurple
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    NotePurple.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    "Close",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.height(22.dp))

        // SEARCH
        OutlinedTextField(
            value = query,
            onValueChange = notesViewModel::updateSearch,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    "Search notes, marks, links...",
                    color = NoteMuted
                )
            },
            leadingIcon = {
                Text(
                    "⌕",
                    color = NotePurple,
                    fontSize = 26.sp
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = ForgeColors.TextPrimary,
                unfocusedTextColor = ForgeColors.TextPrimary,
                focusedBorderColor = NotePurple,
                unfocusedBorderColor = NoteBorder,
                cursorColor = NotePurple,
                focusedContainerColor = NoteCard,
                unfocusedContainerColor = NoteCard
            )
        )

        Spacer(Modifier.height(18.dp))

        // NOTES COUNT AND ADD BUTTON
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "${notes.size} Notes",
                    color = ForgeColors.TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    "$pinnedCount pinned",
                    color = NoteMuted,
                    fontSize = 11.sp
                )
            }

            Button(
                onClick = {
                    editingNote = null
                    showEditor = true
                },
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ForgeColors.Primary,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 11.dp
                )
            ) {
                Text(
                    "+ New Note",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // NOTES LIST / EMPTY STATE
        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(NoteCard)
                        .border(
                            1.dp,
                            NoteBorder,
                            RoundedCornerShape(24.dp)
                        )
                        .padding(26.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .background(
                                ForgeColors.Primary.copy(alpha = 0.14f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "▤",
                            color = NotePurple,
                            fontSize = 32.sp
                        )
                    }

                    Spacer(Modifier.height(18.dp))

                    Text(
                        if (query.isBlank()) {
                            "Your space is empty"
                        } else {
                            "No matching notes"
                        },
                        color = ForgeColors.TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(7.dp))

                    Text(
                        if (query.isBlank()) {
                            "Create your first note and keep important information in one place."
                        } else {
                            "Try another keyword to find your notes."
                        },
                        color = NoteMuted,
                        fontSize = 12.sp,
                        lineHeight = 19.sp
                    )

                    if (query.isBlank()) {
                        Spacer(Modifier.height(18.dp))

                        Button(
                            onClick = {
                                editingNote = null
                                showEditor = true
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ForgeColors.Primary
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Create your first note")
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(13.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(
                    items = notes,
                    key = { it.id }
                ) { note ->
                    NoteCard(
                        note = note,
                        onPin = {
                            notesViewModel.togglePin(note)
                        },
                        onEdit = {
                            editingNote = note
                            showEditor = true
                        },
                        onDelete = {
                            noteToDelete = note
                        }
                    )
                }
            }
        }
    }

    // DELETE CONFIRMATION
    noteToDelete?.let { note ->
        AlertDialog(
            onDismissRequest = {
                noteToDelete = null
            },
            containerColor = NoteCard,
            title = {
                Text(
                    "Delete note?",
                    color = ForgeColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${note.title}\"? This action cannot be undone.",
                    color = NoteMuted
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        notesViewModel.deleteNote(note)
                        noteToDelete = null
                    }
                ) {
                    Text(
                        "Delete",
                        color = ForgeColors.Error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        noteToDelete = null
                    }
                ) {
                    Text(
                        "Cancel",
                        color = NoteMuted
                    )
                }
            }
        )
    }

    // CREATE / EDIT NOTE
    if (showEditor) {
        NoteEditorDialog(
            note = editingNote,
            onDismiss = {
                showEditor = false
            },
            onSave = { title, content, category, pinned ->
                val existing = editingNote

                if (existing == null) {
                    notesViewModel.addNote(
                        title,
                        content,
                        category,
                        pinned
                    )
                } else {
                    notesViewModel.updateNote(
                        existing.copy(
                            title = title,
                            content = content,
                            category = category,
                            isPinned = pinned
                        )
                    )
                }

                showEditor = false
            }
        )
    }
}

@Composable
private fun NoteCard(
    note: Note,
    onPin: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(
            containerColor = NoteCard
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (note.isPinned) {
                NotePurple.copy(alpha = 0.45f)
            } else {
                NoteBorder
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(11.dp)
        ) {

            // TITLE AND PIN
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        note.title.ifBlank { "Untitled note" },
                        color = ForgeColors.TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        note.category.uppercase(),
                        color = NotePurple,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                }

                if (note.isPinned) {
                    Text(
                        "📌 PINNED",
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                ForgeColors.Primary.copy(alpha = 0.15f)
                            )
                            .padding(
                                horizontal = 9.dp,
                                vertical = 6.dp
                            ),
                        color = NotePurple,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // NOTE CONTENT
            Text(
                text = note.content.ifBlank {
                    "No additional content"
                },
                color = NoteMuted,
                fontSize = 13.sp,
                lineHeight = 20.sp,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            HorizontalDivider(
                color = NoteBorder.copy(alpha = 0.7f)
            )

            // ACTIONS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onPin,
                    contentPadding = PaddingValues(
                        horizontal = 8.dp
                    )
                ) {
                    Text(
                        if (note.isPinned) "Unpin" else "Pin",
                        color = NotePurple,
                        fontSize = 12.sp
                    )
                }

                TextButton(
                    onClick = onEdit,
                    contentPadding = PaddingValues(
                        horizontal = 8.dp
                    )
                ) {
                    Text(
                        "Edit",
                        color = NoteMuted,
                        fontSize = 12.sp
                    )
                }

                TextButton(
                    onClick = onDelete,
                    contentPadding = PaddingValues(
                        horizontal = 8.dp
                    )
                ) {
                    Text(
                        "Delete",
                        color = ForgeColors.Error,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun NoteEditorDialog(
    note: Note?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, Boolean) -> Unit
) {
    var title by remember(note) {
        mutableStateOf(note?.title ?: "")
    }

    var content by remember(note) {
        mutableStateOf(note?.content ?: "")
    }

    var category by remember(note) {
        mutableStateOf(note?.category ?: "General")
    }

    var pinned by remember(note) {
        mutableStateOf(note?.isPinned ?: false)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = NoteCard,
        title = {
            Column {
                Text(
                    if (note == null) "Create Note" else "Edit Note",
                    color = ForgeColors.TextPrimary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    "Capture something important.",
                    color = NoteMuted,
                    fontSize = 12.sp
                )
            }
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Title")
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(13.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Next
                    ),
                    colors = noteFieldColors()
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 130.dp),
                    label = {
                        Text("Write your note...")
                    },
                    minLines = 5,
                    shape = RoundedCornerShape(13.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    ),
                    colors = noteFieldColors()
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {
                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Category")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(13.dp),
                        colors = noteFieldColors()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        noteCategories.forEach { item ->
                            DropdownMenuItem(
                                text = {
                                    Text(item)
                                },
                                onClick = {
                                    category = item
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            "Pin this note",
                            color = ForgeColors.TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            "Keep it easy to find",
                            color = NoteMuted,
                            fontSize = 11.sp
                        )
                    }

                    Switch(
                        checked = pinned,
                        onCheckedChange = {
                            pinned = it
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = ForgeColors.Primary
                        )
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() || content.isNotBlank()) {
                        onSave(
                            title,
                            content,
                            category,
                            pinned
                        )
                    }
                },
                enabled = title.isNotBlank() || content.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ForgeColors.Primary
                ),
                shape = RoundedCornerShape(11.dp)
            ) {
                Text("Save Note")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    "Cancel",
                    color = NoteMuted
                )
            }
        }
    )
}

@Composable
private fun noteFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = ForgeColors.TextPrimary,
        unfocusedTextColor = ForgeColors.TextPrimary,
        focusedBorderColor = NotePurple,
        unfocusedBorderColor = NoteBorder,
        focusedLabelColor = NotePurple,
        unfocusedLabelColor = NoteMuted,
        cursorColor = NotePurple,
        focusedContainerColor = ForgeColors.Background,
        unfocusedContainerColor = ForgeColors.Background
    )
