package com.example.trivialapproom.ui.state

import com.example.trivialapproom.model.Question

sealed interface TrivialUiState {
    data object Loading : TrivialUiState
    data class Error(val message: String) : TrivialUiState
    data object Success : TrivialUiState
    data object Home : TrivialUiState
}

data class TrivialViewState(
    val uiState: TrivialUiState = TrivialUiState.Loading,
    val questions: List<Question> = emptyList(),
    val playerName: String = "",
    val expanded: Boolean = false,
    val category: Category = categories.first(),
    val currentQuestionIndex: Int = 0,
    val correctAnswers: Int = 0,
    val numberOfQuestions: Int = 5,
    val questionReplied: Boolean = false,
    val currentQuestion: Question? = null,
    val currentPercentage: Int = 0,
    val actualRecord: Int = 0,
    val gameFinished: Boolean = false,
    val newRecord: Boolean = false,
)

data class Category(
    val id: Int,
    val name: String,
)

val categories = listOf(
    Category(10, "Books"),
    Category(11, "Film"),
    Category(12, "Music"),
    Category(13, "Musicals & Theatres"),
    Category(14, "Television"),
    Category(15, "Video Games"),
    Category(16, "Board Games"),
)