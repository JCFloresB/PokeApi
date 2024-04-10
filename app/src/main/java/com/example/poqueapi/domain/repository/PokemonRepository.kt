package com.example.poqueapi.domain.repository

import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.domain.model.PokemonInitData
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<PokemonInitData>

    fun getPokemon(id: Int): Flow<Result<Pokemon>>
}