@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.devarag.forge.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devarag.forge.data.entity.Note
import com.devarag.forge.ui.viewmodel.NotesViewModel

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
    notesViewModel: NotesViewModel = viewModel()
) {
    val notes by notesViewModel.notes.collectAsState()
    val query by notesViewModel.query.collectAsState()

    var showEditor by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<Note?>(null) }
    var noteToDelete by remember { mutableStateOf<Note?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Forge Notes",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    "Your personal knowledge space",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            TextButton(onClick = onClose) {
                Text("Close")
            }
        }

        Spacer(Modifier.height(18.dp))

        OutlinedTextField(
            value = query,
            onValueChange = notesViewModel::updateSearch,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text("Search notes, marks, links...")
            },
            singleLine = true
        )

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "${notes.size} notes",
                style = MaterialTheme.typography.titleMedium
            )

            Button(
                onClick = {
                    editingNote = null
                    showEditor = true
                }
            ) {
                Text("+ New Note")
            }
        }

        Spacer(Modifier.height(12.dp))

        if (notes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text(
                    if (query.isBlank()) {
                        "No notes yet. Create your first note."
                    } else {
                        "No notes match your search."
                    }
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(notes, key = { it.id }) { note ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        note.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        note.category,
                                        color = MaterialTheme.colorScheme.primary,
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }

                                if (note.isPinned) {
                                    Text(
                                        "📌 PINNED",
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Text(
                                note.content,
                                style = MaterialTheme.typography.bodyMedium,
                                maxLines = 4
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        notesViewModel.togglePin(note)
                                    }
                                ) {
                                    Text(if (note.isPinned) "Unpin" else "Pin")
                                }

                                TextButton(
                                    onClick = {
                                        editingNote = note
                                        showEditor = true
                                    }
                                ) {
                                    Text("Edit")
                                }

                                TextButton(
                                    onClick = {
                                        noteToDelete = note
                                    }
                                ) {
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Delete confirmation dialog
    noteToDelete?.let { note ->
        AlertDialog(
            onDismissRequest = { noteToDelete = null },
            title = {
                Text(
                    text = "Delete note?",
                    color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                    text = "Are you sure you want to delete \"${note.title}\"? This action cannot be undone.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { noteToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }

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
        title = {
            Text(if (note == null) "Create Note" else "Edit Note")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Title") },
                    singleLine = true
                )

                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 130.dp),
                    label = { Text("Write your note...") },
                    minLines = 5
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
                        label = { Text("Category") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {
                        noteCategories.forEach { item ->
                            DropdownMenuItem(
                                text = { Text(item) },
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
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Pin this note")

                    Switch(
                        checked = pinned,
                        onCheckedChange = { pinned = it }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isNotBlank() || content.isNotBlank()) {
                        onSave(title, content, category, pinned)
                    }
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
}