package com.example.notes.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

data class Note(val id: Long, val content: String)

class NotesDatabase(context: Context) : SQLiteOpenHelper(context, "notes.db", null, 2)
{

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            create table notes (
                id      integer primary key autoincrement,
                content text not null
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("alter table notes rename to notes_old")
            onCreate(db)

            db.execSQL(
                """
                insert into notes (id, content) select id, content from notes_old
                """
            )

        }
    }

    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = readableDatabase
        db
        .rawQuery("select id, content from notes order by id desc", null)
        .use { cursor ->
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
        db.execSQL("insert into notes (content) values (?)", arrayOf(content))
    }
}
