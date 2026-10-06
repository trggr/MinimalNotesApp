package com.example.notes.android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.notes.data.NotesDatabase
import com.example.notes.data.NoteEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        NotesDatabase::class.java,
        "notes.db"
    ).build()

    private val dao = db.noteDao()

    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notes: StateFlow<List<NoteEntity>> = _notes.asStateFlow()

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            _notes.value = dao.getAllNotes()
        }
    }

    fun addNote(content: String) {
        viewModelScope.launch {
            dao.insertNote(NoteEntity(content = content))
            loadNotes()
        }
    }
}