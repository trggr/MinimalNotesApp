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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NotesScreen(viewModel)
                }
            }
        }
    }
}

@Composable
fun NotesScreen(viewModel: MainViewModel) {
    val notes by viewModel.notes.collectAsState()
    var textInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.height(24.dp))
        Text("Minimal Notes", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            label = { Text("Enter a note...") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.addNote(textInput)
                textInput = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Note")
        }

        Spacer(modifier = Modifier.height(24.dp))
        Divider()
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(modifier = Modifier.weight(1f).fillMaxWidth()) {
            items(notes) { note ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                ) {
                    Text(text = note, modifier = Modifier.padding(16.dp))
                }
            }
        }
    }
}
