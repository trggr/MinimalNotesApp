package com.example.notes.domain

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.notes.data.NoteDao
import com.example.notes.data.NoteEntity
import com.example.notes.data.NotesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class NoteRepository(private val dbBuilder: RoomDatabase.Builder<NotesDatabase>) {
    private val database: NotesDatabase by lazy {
        dbBuilder
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    private val dao: NoteDao get() = database.noteDao()

    suspend fun getNotes(): List<String> = withContext(Dispatchers.IO) {
        dao.getAllNotes().map { it.content }
    }

    suspend fun addNote(content: String) = withContext(Dispatchers.IO) {
        if (content.isNotBlank()) {
            dao.insertNote(NoteEntity(content = content))
        }
    }
}
