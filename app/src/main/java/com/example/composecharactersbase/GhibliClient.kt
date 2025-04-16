package com.example.composecharactersbase

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GhibliClient {
    private const val BASE_URL = "https://ghibliapi.vercel.app/"

    val service: GhibliService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GhibliService::class.java)
    }
}