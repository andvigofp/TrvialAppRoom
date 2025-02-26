package com.example.trivialapproom.data

import com.example.trivialapproom.model.QuestionApi
import com.example.trivialapproom.network.TriviaApiService

interface QuestionRepository {
    suspend fun getQuestions(quantity: Int, category: Int): List<QuestionApi>
}

class NetworkQuestionsRepository(
    private val triviaApiService: TriviaApiService
) : QuestionRepository {
    override suspend fun getQuestions(quantity: Int, category: Int): List<QuestionApi> {
        val response = triviaApiService.getApiQuestions(
            amount = quantity,
            category = category
        )
        return if (response.isSuccessful) {
            response.body()?.results ?: emptyList()
        } else {
            emptyList()
        }
    }
}