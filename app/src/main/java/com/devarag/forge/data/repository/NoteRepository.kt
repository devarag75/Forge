
package com.devarag.forge.data.repository

import com.devarag.forge.data.dao.NoteDao
import com.devarag.forge.data.entity.Note

class NoteRepository(
    private val noteDao: NoteDao
) {
    val allNotes = noteDao.getAllNotes()

    fun searchNotes(query: String) =
        noteDao.searchNotes(query)

    suspend fun addNote(note: Note) {
        noteDao.insertNote(note)
    }

    suspend fun updateNote(note: Note) {
        noteDao.updateNote(
            note.copy(updatedAt = System.currentTimeMillis())
        )
    }

    suspend fun deleteNote(note: Note) {
        noteDao.deleteNote(note)
    }
}
