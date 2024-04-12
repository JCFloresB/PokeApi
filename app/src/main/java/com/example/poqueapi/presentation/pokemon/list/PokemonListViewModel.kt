package com.example.poqueapi.presentation.pokemon.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.domain.model.Result
import com.example.poqueapi.domain.usecases.GetListPokemon
import com.example.poqueapi.domain.usecases.SetFavoriteValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListUseCase: GetListPokemon,
    private val setFavoriteValueUseCase: SetFavoriteValue,
) : ViewModel() {

    private val _isFavoriteStateFlow = MutableStateFlow<Result<Any>>(Result.Loading())
    val isFavoriteStateFlow: StateFlow<Result<Any>> = _isFavoriteStateFlow

    val pokemonPagingDataFlow: Flow<PagingData<Pokemon>> =
        getPokemonListUseCase().cachedIn(viewModelScope)

    fun modifyFavoriteValue(pokemonId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            Timber.d("favoritos viewmodel")
            setFavoriteValueUseCase(pokemonId, isFavorite).collect { r ->
                _isFavoriteStateFlow.value = r
            }
        }
    }

}