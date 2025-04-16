package com.example.composecharactersbase

import retrofit2.http.GET
import retrofit2.http.Path

interface GhibliService {
    @GET("people")
    suspend fun getCharacters(): List<GhibliCharacter>

    @GET("people/{id}")
    suspend fun getCharacterById(@Path("id") id: String): GhibliCharacter
}