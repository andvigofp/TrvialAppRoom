package com.example.trivialapproom.ui.state


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.trivialapproom.TriviaApplication
import com.example.trivialapproom.data.AppContainer
import com.example.trivialapproom.data.QuestionRepository
import com.example.trivialapproom.data.TriviaPreferencesRepository
import com.example.trivialapproom.data.TrivialGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch


class TrivialViewModel(
    private val questionRepository: QuestionRepository,
    private val trivialPreferencesRepository: TriviaPreferencesRepository,
    private val trivialGameRepository: AppContainer,
) : ViewModel() {
    // Trvial Ui State
    private val _trivialViewState =MutableStateFlow(TrivialViewState())
    val trivialViewState: StateFlow<TrivialViewState> = _trivialViewState.asStateFlow()

    init {
        //Cargar el record de las preferencias
        viewModelScope.launch {
            val record = trivialPreferencesRepository.recordFlow.first()
            _trivialViewState.value = trivialViewState.value.copy(actualRecord = record)
        }

        //Estado incial de HomeScreen
        _trivialViewState.value = _trivialViewState.value.copy(uiState = TrivialUiState.Home)
    }

    fun loadQuestions(playerName: String, quantity: Int, category: Category) {
        viewModelScope.launch {
            _trivialViewState.value = _trivialViewState.value.copy(uiState = TrivialUiState.Loading)
            // val questions = getQuestions(quantity)
            val questions = questionRepository.getQuestions(quantity, category.id).map { it.toQuestion() }
            _trivialViewState.value = _trivialViewState.value.copy(
                uiState = TrivialUiState.Success,
                numberOfQuestions = quantity,
                questions = questions,
                playerName = playerName,
                category = category,
                currentQuestionIndex = 0,
                correctAnswers = 0,
                questionReplied = false,
                currentPercentage = 0,
                gameFinished = false,
                newRecord = false,
                currentQuestion = questions.firstOrNull(),
            )
        }
    }


    fun onAnswerSelect(answer: String) {
        viewModelScope.launch {
            val currentQuestion = _trivialViewState.value.questions.getOrNull(_trivialViewState.value.currentQuestionIndex)

            if (currentQuestion !=null) {
                val correctAnswers = trivialViewState.value.correctAnswers + if (currentQuestion.validateAnswer(
                    answer
                )
                )1 else 0
                _trivialViewState.value = _trivialViewState.value.copy(
                    correctAnswers = correctAnswers,
                    currentPercentage = correctAnswers * 100 / (_trivialViewState.value.currentQuestionIndex + 1),
                    questionReplied = true,
                )
            }
        }
    }

    fun onNextQuestion() {
        viewModelScope.launch {
            val currentQuestionIndex = _trivialViewState.value.currentQuestionIndex + 1
            if (currentQuestionIndex < _trivialViewState.value.numberOfQuestions) {
                _trivialViewState.value = _trivialViewState.value.copy(
                    currentQuestionIndex = currentQuestionIndex,
                    currentQuestion = _trivialViewState.value.questions.getOrNull(currentQuestionIndex),
                    questionReplied = false,
                )
            } else {
                _trivialViewState.value = _trivialViewState.value.copy(gameFinished = true)
                if (_trivialViewState.value.currentPercentage > _trivialViewState.value.actualRecord) {
                    _trivialViewState.value = _trivialViewState.value.copy(
                        actualRecord = _trivialViewState.value.currentPercentage,
                        newRecord = true,
                    )
                    trivialPreferencesRepository.writeTriviaPreferences(_trivialViewState.value.currentPercentage)
                }
            }
        }
    }

    fun onRestartGame() {
        loadQuestions(_trivialViewState.value.playerName, _trivialViewState.value.numberOfQuestions, _trivialViewState.value.category)
    }

    fun decreaseQuantity() {
        if (_trivialViewState.value.numberOfQuestions > 5) _trivialViewState.value = _trivialViewState.value.copy(numberOfQuestions = _trivialViewState.value.numberOfQuestions - 1)
    }

    fun increaseQuantity() {
        if(_trivialViewState.value.numberOfQuestions < 20) _trivialViewState.value = _trivialViewState.value.copy(numberOfQuestions = _trivialViewState.value.numberOfQuestions + 1)
    }

    fun onBackToHome() {
        _trivialViewState.value = _trivialViewState.value.copy(uiState = TrivialUiState.Home)
    }

    fun listOfCategories() = categories

    fun onChangePlayerName(playerName: String) {
        _trivialViewState.value = _trivialViewState.value.copy(playerName = playerName)
    }

    fun onChangeCategory(category: String) {
        _trivialViewState.value = _trivialViewState.value.copy(
            category = categories.first { it.name == category },
            expanded = false,
        )
    }

    fun expandDropDownMenu(expanded: Boolean) {
        _trivialViewState.value = _trivialViewState.value.copy(expanded = expanded)
    }

    fun saveGame(playerName: String, score: Int, category: String) {
        viewModelScope.launch {
            val games = trivialGameRepository.gameRepository.getAllGames().first() // Obtener todos los juegos existentes
            val existingGame = games.find { it.payer == playerName && it.category == category && it.score == score }

            if (existingGame == null) {
                val newGame = TrivialGame(
                    payer = playerName,
                    score = score,
                    category = category
                )
                trivialGameRepository.gameRepository.insertGame(newGame)

                // Imprimir los resultados en el terminal
                println("Game Saved! Player: $playerName, Category: $category, Score: $score")
            } else {
                println("Game already exists for Player: $playerName, Category: $category, Score: $score")
            }
        }
    }


    // Nueva función para obtener todos los juegos desde la base de datos
    fun getAllGames(): Flow<List<TrivialGame>> {
        return trivialGameRepository.gameRepository.getAllGames()
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as TriviaApplication)
                val questionRepository = application.container.questionRepository
                val triviaPreferencesRepository = application.triviaPreferencesRepository
                TrivialViewModel(
                    questionRepository = questionRepository,
                    trivialGameRepository = application.container,
                    trivialPreferencesRepository = triviaPreferencesRepository)
            }
        }
    }
}

