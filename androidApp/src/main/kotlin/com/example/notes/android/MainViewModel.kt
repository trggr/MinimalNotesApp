package com.example.notes.android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.notes.data.NotesDatabase
import com.example.notes.domain.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dbFile = application.getDatabasePath("notes.db")
    
    private val repository = NoteRepository(
        Room.databaseBuilder<NotesDatabase>(
            context = application,
            name = dbFile.absolutePath
        )
    )

    private val _notes = MutableStateFlow<List<String>>(emptyList())
    val notes: StateFlow<List<String>> = _notes.asStateFlow()

    init {
        loadNotes()
    }

    fun loadNotes() {
        viewModelScope.launch {
            _notes.value = repository.getNotes()
        }
    }

    fun addNote(content: String) {
        viewModelScope.launch {
            repository.addNote(content)
            loadNotes()
        }
    }
}
