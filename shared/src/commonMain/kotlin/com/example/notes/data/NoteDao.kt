package com.example.notes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface NoteDao {
    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    suspend fun getAllNotes(): List<NoteEntity>

    @Insert
    suspend fun insertNote(note: NoteEntity)
}
