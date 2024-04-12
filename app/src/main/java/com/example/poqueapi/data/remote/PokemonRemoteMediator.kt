package com.example.poqueapi.data.remote

import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.poqueapi.data.local.PokemonDatabase
import com.example.poqueapi.data.local.entities.PokemonEntity
import com.example.poqueapi.data.local.entities.PokemonRemoteKeyEntity
import com.example.poqueapi.utils.KeyUtils
import com.example.poqueapi.utils.UrlUtils
import timber.log.Timber
import javax.inject.Inject

class PokemonRemoteMediator @Inject constructor(
    private val pokemonDatabase: PokemonDatabase,
    private val pokemonApi: PokemonApi
): RemoteMediator<Int, PokemonEntity>() {
    private val remoteKeyId = "pokemon"

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        return try {
            val offset = when (loadType) {
                LoadType.REFRESH -> {
                    Timber.d("PokemonRemoteMediator REFRESH")
                    0
                }
                LoadType.PREPEND -> {
                    Timber.d("PokemonRemoteMediator PREPEND")
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                LoadType.APPEND -> {
                    Timber.d("PokemonRemoteMediator APPEND")
                    val remoteKey = pokemonDatabase.remoteKeyDao.getRemoteKeyById(remoteKeyId)
                    if (remoteKey == null || remoteKey.nextOffset == 0) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKey.nextOffset
                }
            }

            Timber.d("offset query: $offset, limit query: ${state.config.pageSize}")
            val apiResponse = pokemonApi.getPokemons(offset = offset, limit = state.config.pageSize)
            val listInitPokemonData = apiResponse.results
            val nextOffsetResponse = KeyUtils.getNextKey(apiResponse.next)
            val pokemonList = mutableListOf<PokemonEntity>()

            listInitPokemonData.forEach { pokemonInitData ->
                val pokemonId = UrlUtils.getIdAtEndFromUrl(pokemonInitData.url)

                val getComplementDataResponse = pokemonApi.getPokemonDetailById(pokemonId)
                val typeList = getComplementDataResponse.types

                val pokemonEntity = PokemonEntity(
                    pokemonId = getComplementDataResponse.id,
                    pokemonName = getComplementDataResponse.name,
                    pokemonUrlImage = UrlUtils.getImageUrl(getComplementDataResponse.sprites),
                    pokemonHeight = getComplementDataResponse.height,
                    pokemonWeight = getComplementDataResponse.weight,
                    pokemonTypeList = UrlUtils.getStringTypes(typeList),
                    isFavorite = false,
                )
                pokemonList.add(pokemonEntity)
            }

            pokemonDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    pokemonDatabase.pokemonDao.clearPokemons()
                    pokemonDatabase.remoteKeyDao.deleteRemoteKeyById(remoteKeyId)
                }
                pokemonDatabase.pokemonDao.insertAll(pokemons = pokemonList)
                pokemonDatabase.remoteKeyDao.insert(
                    PokemonRemoteKeyEntity(
                        id = remoteKeyId,
                        nextOffset = nextOffsetResponse
                    )
                )
            }
            Timber.d("Paginacion final pokemonListSize: ${pokemonList.size}, state page Size: ${state.config.pageSize}")
            MediatorResult.Success(nextOffsetResponse == 0)
//            MediatorResult.Success(endOfPaginationReached = pokemonList.size == state.config.pageSize)
        } catch (e: Exception) {
            Timber.e("Error en paginación: ${e.message}")
            MediatorResult.Error(e)
        }
    }
}
