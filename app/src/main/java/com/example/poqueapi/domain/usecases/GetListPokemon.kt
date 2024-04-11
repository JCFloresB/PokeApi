package com.example.poqueapi.domain.usecases

import androidx.paging.PagingData
import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.domain.repository.PokemonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetListPokemon @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {
    operator fun invoke(): Flow<PagingData<Pokemon>> {
        return pokemonRepository.getPokemonList().flowOn(Dispatchers.IO)
    }
}