package com.example.notes.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    notesScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun notesScreen(viewModel: MainViewModel) {
    val notes by viewModel.notes.collectAsState()
    var textInput by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current

    val filteredNotes = remember(notes, searchQuery) {
        if (searchQuery.isBlank()) notes else notes.filter { it.noteTxt.contains(searchQuery, true) }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Spacer(Modifier.height(8.dp))
        Text("Minimal Notes", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            label = { Text("Enter a note...") },
            modifier = Modifier.fillMaxWidth().heightIn(min = 100.dp, max = 180.dp)
        )

        Button(
            onClick = {
                if (textInput.isNotBlank()) {
                    viewModel.addNote(textInput)
                    textInput = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Note")
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Filter notes...") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        HorizontalDivider()

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredNotes, key = { it.noteId }) { note ->
                var confirmDelete by remember { mutableStateOf(false) }


// ... inside your items() block ...

Card(modifier = Modifier.fillMaxWidth()) {
    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp) // Tight gap between metadata and note
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Timestamp takes remaining left space
            Text(
                text = note.formatCretTs,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f),
                maxLines = 1
            )
            
            // 2. Clear, simple text links acting as buttons
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) { // Space between items
                Text(
                    text = if (confirmDelete) "Confirm?" else "Delete",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.clickable { 
                        if (confirmDelete) viewModel.deleteNote(note.noteId) else confirmDelete = true 
                    }
                )
                Text(
                    text = "Copy",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary, // Or any color you prefer
                    modifier = Modifier.clickable { 
                        clipboardManager.setText(AnnotatedString(note.noteTxt)) 
                    }
                )
            }
        }

        // 3. Main Note Content
        Text(
            text = note.noteTxt, 
            style = MaterialTheme.typography.bodyMedium
        )
    }
}




            }
        }
    }
}
