package com.example.myapplication11

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                CharacterApp()  // Start the app
            }
        }
    }
}

// Function to process character data and return a list of formatted strings
fun processCharacterData(
    characterMap: Map<String, String>,
    format: (Map.Entry<String, String>) -> String
): List<String> {
    return characterMap.map { format(it) } // Apply the closure
}

@Composable
fun CharacterApp() {
    // Use mutableStateMapOf to allow state changes and UI recomposition
    val characters = remember { mutableStateMapOf("H" to "Harry Potter", "G" to "Hermione Granger", "R" to "Ron Weasley") }

    // Format character data (code and name) for display
    val formattedCharacters = processCharacterData(characters) { entry ->
        "${entry.key}: ${entry.value}"  // Format each entry as "Code: Name"
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Title text
        Text(text = "Character List", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 8.dp))

        // Display the formatted characters in a lazy column
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(formattedCharacters.size) { index ->
                Text(text = formattedCharacters[index], style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(8.dp))
            }
        }

        // Input field for adding new characters
        val inputText = remember { mutableStateOf(TextFieldValue("")) }

        Spacer(modifier = Modifier.height(16.dp))  // Add space between input and buttons

        // Input field for character name
        OutlinedTextField(
            value = inputText.value,
            onValueChange = { inputText.value = it },
            label = { Text("Enter character name") },
            modifier = Modifier.fillMaxWidth()  // Fill the width of the screen
        )

        // Row for the "Add" and "Delete" buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                // Add a new character with the first letter of the input as the key
                if (inputText.value.text.isNotEmpty()) {
                    val newCode = inputText.value.text.first().toUpperCase().toString()  // Use the first letter as the key
                    characters[newCode] = inputText.value.text  // Add the character to the map
                    inputText.value = TextFieldValue("")  // Clear the input field after adding
                }
            }) {
                Text("Add")  // Label for the add button
            }

            Button(onClick = {
                // Remove the last character from the list (if any)
                if (characters.isNotEmpty()) {
                    val lastKey = characters.keys.last()  // Get the last key in the map
                    characters.remove(lastKey)  // Remove the character with that key
                }
            }) {
                Text("Delete")  // Label for the delete button
            }
        }
    }
}
