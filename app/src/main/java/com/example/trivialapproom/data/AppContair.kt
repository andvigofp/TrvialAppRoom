package com.example.trivialapproom.data

import android.content.Context
import com.example.trivialapproom.network.TriviaApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val questionRepository: QuestionRepository
    val gameRepository: TrvialGameRepoitorio

}

class DefaultAppcontainer(context: Context): AppContainer {
    private val baseUrl = "https://opentdb.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: TriviaApiService by lazy { retrofit.create(TriviaApiService::class.java) }

    override val questionRepository: QuestionRepository by lazy { NetworkQuestionsRepository(retrofitService) }

    // Aquí empiezo con la parte de Room
    private val appDatabase = AppDatabase.getDatabase(context)
    private val gameDao = appDatabase.gameDao()
    override val gameRepository: TrvialGameRepoitorio by lazy { TrvialRepositoroImpl(gameDao) }

    fun provideGameRepository(): TrvialGameRepoitorio {
        return gameRepository
    }
}