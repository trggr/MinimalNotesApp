package com.example.notes.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

data class Note(val noteId: Long, val noteTxt: String, val cretTs: String?) {
    val formattedTimestamp: String
        get() {
            if (cretTs.isNullOrEmpty()) return ""
            return try {
                // SQLite stores default_current_timestamp in UTC ("yyyy-MM-dd HH:mm:ss")
                val utcFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                val date = utcFormat.parse(cretTs) ?: return cretTs
                
                // Format to MM/dd/yyyy HH:mm:ss in local time
                val targetFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault()).apply {
                    timeZone = TimeZone.getDefault()
                }
                targetFormat.format(date)
            } catch (e: Exception) {
                cretTs
            }
        }
}

class NotesDatabase(context: Context) : SQLiteOpenHelper(context, "notes.db", null, 6)
{

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
        if (oldVersion < 5) {
            db.execSQL("drop table if exists note_old")
            db.execSQL("alter table note rename to note_old")
            onCreate(db)

            // db.execSQL(
            //     """
            //     insert into note (note_id, note_txt, cret_ts) select id, content, cret_ts from notes_old
            //     """
            // )

        }
    }

    fun getAllNotes(): List<Note> {
        val notes = mutableListOf<Note>()
        val db = readableDatabase
db
        .rawQuery("select note_id, note_txt, cret_ts from note order by note_id desc", null)
        .use { cursor ->
            val i = cursor.getColumnIndex("note_id")
            val t = cursor.getColumnIndex("note_txt")
            val c = cursor.getColumnIndex("cret_ts")
            while (cursor.moveToNext()) {
                notes.add(Note(cursor.getLong(i), cursor.getString(t), cursor.getString(c)))
            }
        }
        return notes
    }

    fun insertNote(noteTxt: String) {
        val db = writableDatabase
        db.execSQL("insert into note (note_txt, cret_ts) values (?, datetime('now'))", arrayOf(noteTxt))
    }
}
