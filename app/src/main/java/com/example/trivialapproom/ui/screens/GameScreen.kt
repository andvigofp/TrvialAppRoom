package com.example.trivialapproom.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trivialapproom.model.Question
import com.example.trivialapproom.ui.state.Category
import com.example.trivialapproom.ui.state.TrivialUiState
import com.example.trivialapproom.ui.state.TrivialViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    gameViewModel: TrivialViewModel = viewModel(factory = TrivialViewModel.Factory),
) {
    // Game view model
    val gameViewState by gameViewModel.trivialViewState.collectAsState()

    Scaffold(
        topBar = {
            // Game top bar
            TopAppBar(
                title = { Text("Trivia App") },
                colors = TopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    scrolledContainerColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        bottomBar = {
            // Game bottom bar
            if (gameViewState.uiState == TrivialUiState.Success) {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Question: ${gameViewState.currentQuestionIndex + 1}/${gameViewState.numberOfQuestions}")
                        Text(
                            "Correct Answers: ${gameViewState.currentPercentage} %",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    ) {
        // Game content
        Column(
            modifier = Modifier.padding(it).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,

        ) {

            Spacer(modifier = Modifier.height(16.dp)) // Añadir espacio después de "Player:"

            Text("Player: ${gameViewState.playerName}", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(16.dp)) // Añadir espacio después de "Player:"

            // Game UI
            when (gameViewState.uiState) {
                TrivialUiState.Home -> {
                    // Home state
                    HomeScreen(
                        increseQuantity = { gameViewModel.increaseQuantity() },
                        decreaseQuantity = { gameViewModel.decreaseQuantity() },
                        quantity = gameViewState.numberOfQuestions,
                        playerName = gameViewState.playerName,
                        onChangePlayerName = { playerName -> gameViewModel.onChangePlayerName(playerName) },
                        category = gameViewState.category,
                        onChangeCategory = { category -> gameViewModel.onChangeCategory(category) },
                        categories = gameViewModel.listOfCategories(),
                        expanded = gameViewState.expanded,
                        expandDropDownMenu = { expanded -> gameViewModel.expandDropDownMenu(expanded) },
                        onStartGame = { playerName: String, quantity: Int, category: Category ->
                            gameViewModel.loadQuestions(playerName,quantity, category)
                        },
                        record = gameViewState.actualRecord,
                    )
                }
                TrivialUiState.Loading -> {
                    // Loading state
                    Text("Loading...")
                }
                is TrivialUiState.Error -> {
                    // Error state
                    Text("Error: ${(gameViewState.uiState as TrivialUiState.Error).message}")
                }
                TrivialUiState.Success -> {
                    // Game state
                    GameZone(
                        question = gameViewState.currentQuestion,
                        questionReplied = gameViewState.questionReplied,
                        gameFinished = gameViewState.gameFinished,
                        newRecord = gameViewState.newRecord,
                        currentPercentage = gameViewState.currentPercentage,
                        player = gameViewState.playerName,  // Añadido
                        category = gameViewState.category.name,  // Añadido
                        onAnswerSelected = { gameViewModel.onAnswerSelect(it) },
                        onNextQuestion = { gameViewModel.onNextQuestion() },
                        onRestartGame = { gameViewModel.onRestartGame() },
                        onBackToHome = { gameViewModel.onBackToHome() },
                        saveGame = { player, score, category -> gameViewModel.saveGame(player, score, category) }  // Añadido: función lambda para guardar el juego
                    )
                }
            }
        }
    }
}

@Composable
fun GameZone(
    question: Question?,
    questionReplied: Boolean,
    gameFinished: Boolean,
    newRecord: Boolean,
    currentPercentage: Int = 0,
    player: String,
    category: String,
    onAnswerSelected: (String) -> Unit,
    onNextQuestion: () -> Unit,
    onRestartGame: () -> Unit,
    onBackToHome: () -> Unit,
    saveGame: (String, Int, String) -> Unit,
) {
    // Estado para almacenar la respuesta seleccionada
    var selectedAnswer by remember { mutableStateOf<String?>(null) }

    // Game zone
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Game question
        Text(
            question?.question ?: "No question found",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary
        )

        // Game options
        question?.options?.forEach { option ->
            val isCorrectAnswer = option == question?.correctAnswer
            val buttonColor = when {
                questionReplied && option == selectedAnswer -> {
                    if (isCorrectAnswer) Color.Green else Color.Red
                }
                else -> MaterialTheme.colorScheme.secondary
            }

            Button(
                onClick = {
                    onAnswerSelected(option)
                    selectedAnswer = option
                },
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                    containerColor = buttonColor,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondary,
                    disabledContainerColor = buttonColor
                ),
                enabled = !questionReplied
            ) {
                Text(option)
            }
        }

        // Mostrar el resultado solo después de haber seleccionado una respuesta
        if (questionReplied) {
            val isCorrect = selectedAnswer == question?.correctAnswer
            if (isCorrect == true) {
                Text(
                    text = "Correcto!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Green,
                    modifier = Modifier.padding(top = 8.dp)
                )
            } else {
                Text(
                    text = "Incorrecto. La respuesta correcta es: ${question?.correctAnswer}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        if (gameFinished) {
            // Final score dialog
            FinalScoreDialog(
                score = currentPercentage,
                newRecord = newRecord,
                player = player,
                category = category,
                onRestartGame = onRestartGame,
                onBackToHome = { onBackToHome() },
                saveGame = saveGame
            )
        } else {
            // Next button
            Button(
                onClick = { onNextQuestion() },
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                enabled = questionReplied,
                colors = ButtonDefaults.buttonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.5f),
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                )
            ) {
                Text("Next")
            }
        }
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FinalScoreDialog(
    score: Int,
    newRecord: Boolean,
    player: String,
    category: String,
    onRestartGame: () -> Unit,
    onBackToHome: () -> Unit,
    saveGame: (String, Int, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // Insertar el registro del juego en la base de datos
    saveGame(player, score, category)

    val playerScores = listOf(
        PlayerScore(player, category, score)  // Añadido: agregar el puntaje del jugador a la lista
    )

    AlertDialog(
        onDismissRequest = { onBackToHome() },
        modifier = Modifier.clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surface),
    ) {
        Surface(
            modifier = Modifier.wrapContentWidth().wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                modifier = modifier.padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text("Game finished!", style = MaterialTheme.typography.titleLarge)
                Text("Your score: $score %", style = MaterialTheme.typography.titleMedium)
                if (newRecord) {
                    Text(
                        "New record!",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }

                // Mostrar la tabla de puntajes del jugador
                PlayerScoreTable(playerScores = playerScores)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(
                        onClick = { onRestartGame() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Try Again")
                    }
                    TextButton(
                        onClick = { onBackToHome() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Go to Home")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FinalScoreDialogPreview() {
    FinalScoreDialog(
        score = 100,
        newRecord = true,
        player = "PlayerName",
        category = "CategoryName",
        onRestartGame = {},
        onBackToHome = {},
        saveGame = { _, _, _ -> }
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    GameScreen()
}
