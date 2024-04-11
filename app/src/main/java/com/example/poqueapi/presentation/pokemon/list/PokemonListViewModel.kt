package com.example.poqueapi.presentation.pokemon.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.domain.usecases.GetListPokemon
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListUseCase: GetListPokemon
): ViewModel() {
    val pokemonPagingDataFlow: Flow<PagingData<Pokemon>> =
        getPokemonListUseCase().cachedIn(viewModelScope)
}