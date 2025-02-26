package com.example.trivialapproom

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.trivialapproom.data.AppContainer
import com.example.trivialapproom.data.DefaultAppcontainer
import com.example.trivialapproom.data.TriviaPreferencesRepository
import com.example.trivialapproom.data.TrvialGameRepoitorio

class TriviaApplication : Application() {
    lateinit var container: AppContainer
    lateinit var triviaPreferencesRepository: TriviaPreferencesRepository
    lateinit var gameRepository: TrvialGameRepoitorio

    override fun onCreate() {
        super.onCreate()
        triviaPreferencesRepository = TriviaPreferencesRepository(dataStore)
        container = DefaultAppcontainer(this)
    }
}

private const val RECORD_PREFERENCES_NAME = "record_preferences"

private val Context.dataStore : DataStore<Preferences> by preferencesDataStore(
    name = RECORD_PREFERENCES_NAME
)