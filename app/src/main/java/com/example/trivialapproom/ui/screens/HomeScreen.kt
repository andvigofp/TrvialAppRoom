package com.example.trivialapproom.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.trivialapproom.ui.state.Category

@Composable
fun HomeScreen(
    increseQuantity: () -> Unit,
    decreaseQuantity: () -> Unit,
    quantity: Int = 5,
    onStartGame: (String, Int, Category) -> Unit,
    playerName: String,
    onChangePlayerName: (String) -> Unit,
    category: Category,
    onChangeCategory: (String) -> Unit,
    categories: List<Category>,
    expanded: Boolean = false,
    expandDropDownMenu: (Boolean) -> Unit,
    record: Int = 0,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Trivia App", style = MaterialTheme.typography.titleLarge)

        // Espacio entre el título y el slider
        Spacer(modifier = Modifier.height(16.dp))

        // Mostrar número de preguntas
        Text(text = "Preguntas: $quantity")

        // Barra deslizante para seleccionar el número de preguntas
        Slider(
            value = quantity.toFloat(),
            onValueChange = { newValue ->
                if (newValue.toInt() < quantity) {
                    decreaseQuantity()
                } else {
                    increseQuantity()
                }
            },
            valueRange = 5f..20f,
            steps = 14,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp) // Añadir espacio a los lados del deslizador
                .semantics { contentDescription = "Seleccionar número de preguntas" },
        )

        // Espacio entre el slider y el grupo de botones y récord
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            label = { Text("Player Name") },
            value = playerName,
            onValueChange = onChangePlayerName,
        )

        Box(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            TextButton(
                onClick = { expandDropDownMenu(!expanded) }
            ) {
                Text(category.name)
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand dropdown menu"
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expandDropDownMenu(false)
                },
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        onClick = {
                            onChangeCategory(category.name)
                        },
                        text = {
                            Text("Categoría: " + category.name)
                        }
                    )
                }
            }
        }

        Button(
            onClick = {
                onStartGame(playerName, quantity, category)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Start Game")
        }

        Row {
            Text("Actual record: $record %")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        increseQuantity = {},
        decreaseQuantity = {},
        onStartGame = { _, _, _ -> },
        playerName = "Player",
        onChangePlayerName = {},
        category = Category(1, "Category 1"),
        onChangeCategory = {},
        categories = listOf(
            Category(1, "Category 1"),
            Category(2, "Category 2"),
            Category(3, "Category 3"),
        ),
        expanded = false,
        expandDropDownMenu = {},
    )
}
