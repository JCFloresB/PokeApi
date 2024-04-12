package com.example.poqueapi.domain.usecases

import com.example.poqueapi.domain.model.Result
import com.example.poqueapi.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

class SetFavoriteValue @Inject constructor(
    private val pokemonRepository: PokemonRepository
) {

    operator fun invoke(pokemonId: Int, isFavorite: Boolean): Flow<Result<Any>> {
        Timber.d("favoritos UseCase")
        return pokemonRepository.isFavoritePokemon(id = pokemonId, isFavorite = isFavorite)
    }
}