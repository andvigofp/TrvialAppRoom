package com.example.trivialapproom.data

import kotlinx.coroutines.flow.Flow

interface TrvialGameRepoitorio {
    fun getAllGames(): Flow<List<TrivialGame>>
    fun getBestGame(category: String): Flow<TrivialGame>
    suspend fun insertGame(trivialGame: TrivialGame)
}

class TrvialRepositoroImpl(private val trvialDao: TrvialDao) : TrvialGameRepoitorio {
    override fun getAllGames(): Flow<List<TrivialGame>> = trvialDao.getAllGames()
    override fun getBestGame(category: String): Flow<TrivialGame> = trvialDao.getBestGame(category)
    override suspend fun insertGame(trivialGame: TrivialGame) = trvialDao.insertGame(trivialGame)
}