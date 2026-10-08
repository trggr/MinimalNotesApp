package com.example.notes.android

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class Note(val noteId: Long, val noteTxt: String, val cretTs: String?)
{
    val formatCretTs: String
        get() {
            if (cretTs.isNullOrEmpty()) return ""
            return try {
                val utcFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                val date = utcFormat.parse(cretTs) ?: return cretTs
                
                val targetFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault()).apply {
                    timeZone = TimeZone.getDefault()
                }
                targetFormat.format(date)
            } catch (e: Exception) {
                cretTs
            }
        }
}

class NotesDatabase(context: Context) : SQLiteOpenHelper(context, "notes.db", null, 4) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            create table note (
                note_id integer primary key autoincrement,
                note_txt text not null,
                cret_ts timestamp default current_timestamp
            )
            """
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 4) {
            db.execSQL("drop table if exists notes_old")
            db.execSQL("alter table notes rename to notes_old")
            onCreate(db)

            db.execSQL(
                """
                insert into note (note_id, note_txt, cret_ts) 
                select id, content, datetime('now') from notes_old
                """
            )
            db.execSQL("drop table if exists notes_old")
        }
    }

    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        readableDatabase
            .rawQuery("select note_id, note_txt, cret_ts from note order by note_id desc", null)
            .use { c ->
                while (c.moveToNext()) {
                    notes.add(Note(
                        c.getLong(c.getColumnIndex("note_id")),
                        c.getString(c.getColumnIndex("note_txt")),
                        c.getString(c.getColumnIndex("cret_ts"))))
            }
        }
        return notes
    }

    fun insertNote(s: String) {
        writableDatabase.execSQL("insert into note (note_txt) values (?)", arrayOf(s))
    }

    fun deleteNote(n: Long) {
        writableDatabase.delete("note", "note_id = ?", arrayOf(n.toString()))
    }
}