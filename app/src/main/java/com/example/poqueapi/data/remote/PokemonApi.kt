package com.example.poqueapi.data.remote

import com.example.poqueapi.data.remote.dto.GetPokemonResponse
import com.example.poqueapi.data.remote.dto.pokemondetail.PokemonDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {

    @GET("pokemon/?")
    suspend fun getPokemons(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): GetPokemonResponse

    @GET("pokemon/{id}")
    suspend fun getPokemonDetailById(@Path("id") pokemonId: Int): PokemonDetailResponse
}