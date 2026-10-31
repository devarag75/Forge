
package com.devarag.forge.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devarag.forge.data.database.DatabaseProvider
import com.devarag.forge.data.entity.Note
import com.devarag.forge.data.repository.NoteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NotesViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val database = DatabaseProvider.getDatabase(application)
    private val repository = NoteRepository(database.noteDao())

    private val searchQuery = MutableStateFlow("")

    val query: StateFlow<String> = searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val notes: StateFlow<List<Note>> =
        searchQuery
            .debounce(200)
            .flatMapLatest { query ->
                repository.searchNotes(query)
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                emptyList()
            )

    fun updateSearch(query: String) {
        searchQuery.value = query
    }

    fun addNote(
        title: String,
        content: String,
        category: String,
        isPinned: Boolean
    ) {
        if (title.isBlank() && content.isBlank()) return

        viewModelScope.launch {
            repository.addNote(
                Note(
                    title = title.trim().ifBlank { "Untitled Note" },
                    content = content.trim(),
                    category = category,
                    isPinned = isPinned
                )
            )
        }
    }

    fun updateNote(note: Note) {
        if (note.title.isBlank() && note.content.isBlank()) return

        viewModelScope.launch {
            repository.updateNote(
                note.copy(
                    title = note.title.trim().ifBlank { "Untitled Note" },
                    content = note.content.trim()
                )
            )
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    fun togglePin(note: Note) {
        viewModelScope.launch {
            repository.updateNote(
                note.copy(isPinned = !note.isPinned)
            )
        }
    }
}
