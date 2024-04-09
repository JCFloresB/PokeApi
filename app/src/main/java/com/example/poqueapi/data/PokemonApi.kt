package com.example.poqueapi.data

import retrofit2.http.GET
import retrofit2.http.Query

interface PokemonApi {

    @GET("/pokemon/?")
    suspend fun getPokemon(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    )
}