package com.example.poqueapi.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.map
import com.example.poqueapi.data.local.PokemonDatabase
import com.example.poqueapi.data.local.entities.PokemonEntity
import com.example.poqueapi.data.mapper.toPokemon
import com.example.poqueapi.data.remote.PokemonApi
import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.domain.model.Result
import com.example.poqueapi.domain.repository.PokemonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import javax.inject.Inject

class PokemonRepositoryImp @Inject constructor(
    private val pokemonPager: Pager<Int, PokemonEntity>,
    private val pokemonDatabase: PokemonDatabase,
    private val pokemonApi: PokemonApi,
): PokemonRepository {
    override fun getPokemonList(): Flow<PagingData<Pokemon>> {
        return pokemonPager.flow.map { paginData ->
            paginData.map { it.toPokemon() }
        }
    }

    override fun getPokemon(id: Int): Flow<Result<Pokemon>> = flow {
        val localData = pokemonDatabase.pokemonDao.getPokemonById(id)
        if (localData != null) emit(Result.Loading(localData))

        try {
            // MAKE API CALL & INSERT IT TO DATABASE & EMIT IT
            /*val remoteData = pokemonApi.getPokemonDetailById(id).toPokemon()
            pokemonDatabase.pokemonDao.insert(remoteData.toPokemonEntity())*/
            emit(Result.Success(localData!!))
        } catch (e: Exception) {
            emit(Result.Error(error = e.message.toString(), data = localData))
        }
    }

    override fun isFavoritePokemon(id: Int, isFavorite: Boolean): Flow<Result<Any>> = flow {
        try {
            Timber.d("favoritos repository")
            pokemonDatabase.pokemonDao.markFavorites(id, isFavorite)
            emit(Result.Success(id))
        } catch (e: Exception) {
            Timber.d("favoritos repository Error")
            emit(Result.Error(e.message ?: "Ocurrio un error al modificar"))
        }
    }

}