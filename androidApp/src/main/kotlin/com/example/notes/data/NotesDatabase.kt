package com.example.notes.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Note(val id: Long, val content: String)

class NotesDatabase(context: Context) : SQLiteOpenHelper(context, "notes.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE notes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                content TEXT NOT NULL
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS notes")
        onCreate(db)
    }

    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = readableDatabase
        db.rawQuery("SELECT id, content FROM notes ORDER BY id DESC", null).use { cursor ->
            val idIdx = cursor.getColumnIndex("id")
            val contentIdx = cursor.getColumnIndex("content")
            while (cursor.moveToNext()) {
                notes.add(Note(cursor.getLong(idIdx), cursor.getString(contentIdx)))
            }
        }
        return notes
    }

    fun insertNote(content: String) {
        val db = writableDatabase
        db.execSQL("INSERT INTO notes (content) VALUES (?)", arrayOf(content))
    }
}
