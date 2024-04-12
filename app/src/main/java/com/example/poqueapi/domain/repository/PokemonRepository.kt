package com.example.poqueapi.domain.repository

import androidx.paging.PagingData
import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.domain.model.Result
import kotlinx.coroutines.flow.Flow

interface PokemonRepository {
    fun getPokemonList(): Flow<PagingData<Pokemon>>

    fun getPokemon(id: Int): Flow<Result<Pokemon>>

    fun isFavoritePokemon(id: Int, isFavorite: Boolean): Flow<Result<Any>>
}