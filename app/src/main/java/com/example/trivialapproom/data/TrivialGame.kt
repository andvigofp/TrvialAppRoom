package com.example.trivialapproom.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trivialGame")
data class TrivialGame(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val payer: String,
    val score: Int,
    val category: String,
)
