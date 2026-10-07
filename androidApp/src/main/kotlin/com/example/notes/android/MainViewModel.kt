package com.example.notes.android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = NotesDatabase(application)

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    init {
        loadNotes()
    }

    fun loadNotes() {
        _notes.value = db.getAllNotes()
    }

    fun addNote(noteTxt: String) {
        db.insertNote(noteTxt)
        loadNotes()
    }

    fun deleteNote(noteId: Long) {
        db.deleteNote(noteId)
        loadNotes()
    }
}

