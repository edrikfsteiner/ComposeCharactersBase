package com.example.composecharactersbase

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GhibliRepository(private val context: Context) {
    private val service = GhibliClient.service
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("favorites", Context.MODE_PRIVATE)

    suspend fun getCharacters(): List<GhibliCharacter> {
        return withContext(Dispatchers.IO) {
            service.getCharacters()
        }
    }

    suspend fun getCharacterById(id: String): GhibliCharacter {
        return withContext(Dispatchers.IO) {
            service.getCharacterById(id)
        }
    }

    fun addFavorite(characterId: String) {
        sharedPreferences.edit().putBoolean(characterId, true).apply()
    }

    fun removeFavorite(characterId: String) {
        sharedPreferences.edit().remove(characterId).apply()
    }

    fun isFavorite(characterId: String): Boolean {
        return sharedPreferences.getBoolean(characterId, false)
    }

    fun getAllFavoriteIds(): Set<String> {
        return sharedPreferences.all.mapNotNull {
            if (it.value as? Boolean == true) it.key else null
        }.toSet()
    }
}