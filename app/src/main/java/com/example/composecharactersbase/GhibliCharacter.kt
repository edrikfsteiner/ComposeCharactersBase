package com.example.composecharactersbase

data class GhibliCharacter(
    val id: String,
    val name: String,
    val gender: String = "",
    val age: String = "",
    val eye_color: String = "",
    val hair_color: String = "",
    val films: List<String> = emptyList(),
    val imageUrl: String = ""
)