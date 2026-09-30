package com.example.notes

import androidx.room.Room
import com.example.notes.data.NotesDatabase
import com.example.notes.domain.NoteRepository
import kotlinx.coroutines.runBlocking
import java.io.File

fun main(args: Array<String>) = runBlocking {
    val dbFile = File("notes_cli.db")
    println("Using SQLite database at: ${dbFile.absolutePath}")

    val builder = Room.databaseBuilder<NotesDatabase>(
        name = dbFile.absolutePath
    )
    val repository = NoteRepository(builder)

    val command = args.getOrNull(0) ?: "list"

    when (command) {
        "add" -> {
            val content = args.getOrNull(1)
            if (content.isNullOrBlank()) {
                println("Error: Please provide note content. Example: ./gradlew :shared:runCli --args=\"add 'Hello Linux'\"")
            } else {
                repository.addNote(content)
                println("Successfully added note: \"$content\"")
            }
        }
        "list" -> {
            val notes = repository.getNotes()
            println("\n--- Saved Notes (${notes.size}) ---")
            if (notes.isEmpty()) {
                println("(No notes found)")
            } else {
                notes.forEachIndexed { index, note ->
                    println("${index + 1}. $note")
                }
            }
            println("-----------------------------")
        }
        else -> {
            println("Unknown command: $command")
            println("Usage:")
            println("  ./gradlew :shared:runCli --args=\"list\"")
            println("  ./gradlew :shared:runCli --args=\"add 'Your note text'\"")
        }
    }
}
