package com.example.trivialapproom.network

import com.example.trivialapproom.model.Question
import com.example.trivialapproom.model.QuestionApi
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TriviaApiService{
    @GET("api.php")
    suspend fun getApiQuestions(
        @Query("amount") amount: Int = 10,
        @Query("type") type: String = "multiple",
        @Query("category") category: Int
    ): Response<QuestionApiResponse>
}

@Serializable
data class QuestionApiResponse(
    val response_code: Int,
    val results: List<QuestionApi>
)
