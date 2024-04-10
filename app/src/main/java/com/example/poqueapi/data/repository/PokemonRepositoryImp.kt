package com.example.poqueapi.data.repository

import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.domain.model.PokemonInitData
import com.example.poqueapi.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow

class PokemonRepositoryImp: PokemonRepository {
    override fun getPokemonList(): Flow<PokemonInitData> {
        TODO("Not yet implemented")
    }

    override fun getPokemon(id: Int): Flow<Result<Pokemon>> {
        TODO("Not yet implemented")
    }
}