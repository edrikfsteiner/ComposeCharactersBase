package com.example.composecharactersbase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterViewModel(private val repository: GhibliRepository) : ViewModel() {

    private val _characters = MutableStateFlow<List<GhibliCharacter>>(emptyList())
    val characters: StateFlow<List<GhibliCharacter>> = _characters.asStateFlow()

    private val _selectedCharacter = MutableStateFlow<GhibliCharacter?>(null)
    val selectedCharacter: StateFlow<GhibliCharacter?> = _selectedCharacter.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val rawCharacters = repository.getCharacters()
                val enrichedCharacters = rawCharacters.map { character ->
                    character.copy(
                        imageUrl = characterImageMap[character.id] ?: "https://external-content.duckduckgo.com/iu/?u=https%3A%2F%2Fwww.pngitem.com%2Fpimgs%2Fm%2F83-834494_studio-ghibli-logo-vector-hd-png-download.png&f=1&nofb=1&ipt=90f63c15f42eacb15c9f557272884046821d31548f18ca1cd1440b8245c5b001"
                    )
                }
                _characters.value = enrichedCharacters
            } catch (e: Exception) {
                _error.value = "Failed to load characters: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getCharacterById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                _selectedCharacter.value = repository.getCharacterById(id)
            } catch (e: Exception) {
                _error.value = "Failed to load character: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(character: GhibliCharacter) {
        if (isFavorite(character.id)) {
            repository.removeFavorite(character.id)
        } else {
            repository.addFavorite(character.id)
        }
        loadCharacters()
    }

    fun isFavorite(characterId: String): Boolean {
        return repository.isFavorite(characterId)
    }
}

class CharacterViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CharacterViewModel::class.java)) {
            return CharacterViewModel(GhibliRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

private val characterImageMap = mapOf(
    "ba924631-068e-4436-b6de-f3283fa848f0" to "https://i.imgur.com/1YcUG7N.jpeg",
    "cd3d059c-09f4-4ff3-8d63-bc765a5184fa" to "https://i.imgur.com/kl5u4J5.jpeg",
    "ebe40383-aad2-4208-90ab-698f00c581ab" to "https://i.imgur.com/V03aRRL.jpeg",
    "58611129-2dbc-4a81-a72f-77ddfc1b1b49" to "https://i.imgur.com/Nz1T6cs.jpeg",
    "d5dfb320-ec5e-4fc3-a2c2-60af45fe5e78" to "https://i.imgur.com/mSRbWKe.jpeg",
    "ef5ee7c5-6ea0-41c7-8e15-bb3e61eeb40d" to "https://i.imgur.com/kDQ4xzX.jpeg",
    "758bf02e-3122-46e0-884e-67cf83df1786" to "https://i.imgur.com/jIqXv9b.jpeg",
    "a226f1f0-93c6-407f-92f4-1b19f35aef3a" to "https://i.imgur.com/vd9FlYH.jpeg",
    "f467e18e-3694-409f-abc9-8e5f58f2f1f1" to "https://i.imgur.com/FACg0kP.jpeg"
)
