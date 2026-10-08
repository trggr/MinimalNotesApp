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

                Card(modifier = Modifier.fillMaxWidth()) {
                    // Keep ALL your content inside this Column so things stack vertically
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // The Top Metadata Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween, // Pushes text left, buttons right
                            verticalAlignment = Alignment.CenterVertically // Keeps everything vertically aligned
                        ) {
                            // 1. Timestamp (Takes up remaining space on the left, scales down if needed)
                            Text(
                                text = note.formatCretTs,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.weight(1f), // Crucial: dynamically sizes text so it doesn't push buttons out
                                maxLines = 1
                            )

                            // 2. Button Container (Keeps both buttons strictly together)
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Delete Button
                                TextButton(
                                    onClick = {
                                        if (confirmDelete) viewModel.deleteNote(note.noteId) else confirmDelete = true
                                    },
                                    contentPadding = PaddingValues(
                                        horizontal = 8.dp,
                                        vertical = 0.dp
                                    ), // Tiny padding to fit small spaces
                                    modifier = Modifier.defaultMinSize(
                                        minWidth = 1.dp,
                                        minHeight = 1.dp
                                    ) // Removes huge default button sizing
                                ) {
                                    Text(
                                        text = if (confirmDelete) "Confirm?" else "Delete",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }

                                // Copy Button
                                TextButton(
                                    onClick = { clipboardManager.setText(AnnotatedString(note.noteTxt)) },
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                    modifier = Modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp)
                                ) {
                                    Text(
                                        text = "Copy",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        // 3. The actual note text (Now safely inside the Column below the metadata row)
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
