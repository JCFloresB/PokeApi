package com.example.poqueapi.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.poqueapi.data.local.entities.PokemonEntity

@OptIn(ExperimentalPagingApi::class)
class PokemonRemoteMediator: RemoteMediator<Int, PokemonEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonEntity>
    ): MediatorResult {
        return MediatorResult.Error(Throwable())
    }
}