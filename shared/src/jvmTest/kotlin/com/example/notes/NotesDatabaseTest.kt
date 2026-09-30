package com.example.notes

import androidx.room.Room
import com.example.notes.data.NotesDatabase
import com.example.notes.domain.NoteRepository
import kotlinx.coroutines.runBlocking
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NotesDatabaseTest {

    @Test
    fun testNoteInsertionAndRetrievalOnLinux() = runBlocking {
        val dbFile = File.createTempFile("test_notes", ".db")
        dbFile.deleteOnExit()

        val builder = Room.databaseBuilder<NotesDatabase>(
            name = dbFile.absolutePath
        )

        val repository = NoteRepository(builder)

        assertTrue(repository.getNotes().isEmpty())

        repository.addNote("Hello from Linux CLI test!")

        val notes = repository.getNotes()
        assertEquals(1, notes.size)
        assertEquals("Hello from Linux CLI test!", notes[0])
    }
}
