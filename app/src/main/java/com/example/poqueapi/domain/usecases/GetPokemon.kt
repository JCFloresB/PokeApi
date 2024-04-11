package com.example.poqueapi.domain.usecases

import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.domain.model.Result
import com.example.poqueapi.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetPokemon @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke(id: Int): Flow<Result<Pokemon>> {
        return pokemonRepository.getPokemon(id).flowOn(Dispatchers.IO)
    }
}