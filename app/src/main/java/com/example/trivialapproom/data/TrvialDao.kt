package com.example.trivialapproom.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TrvialDao {
    @Query("SELECT * FROM trivialGame")
    fun getAllGames(): Flow<List<TrivialGame>>

    @Query("SELECT * FROM trivialGame WHERE category = :category ORDER BY score DESC LIMIT 1")
    fun getBestGame(category: String): Flow<TrivialGame>

    @Insert
    suspend fun insertGame(trivialGame: TrivialGame)
}
