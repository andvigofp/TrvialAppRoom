package com.example.trivialapproom.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionApi(
    @SerialName("question")
    val question: String,
    @SerialName("correct_answer")
    val correct_answer: String,
    @SerialName("incorrect_ansers")
    val incorrect_answers: List<String>
) {
    fun toQuestion(): Question {
        val options = incorrect_answers.toMutableList()
        options.add(correct_answer)
        return Question(question, options.shuffled(), correct_answer)
    }
}
