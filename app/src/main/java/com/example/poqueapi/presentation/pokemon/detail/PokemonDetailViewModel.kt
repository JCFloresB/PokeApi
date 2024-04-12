package com.example.poqueapi.presentation.pokemon.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.domain.model.Result
import com.example.poqueapi.domain.usecases.GetPokemon
import com.example.poqueapi.domain.usecases.SetFavoriteValue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPokemonDetailUseCase: GetPokemon,
    private val favoriteValueUseCase: SetFavoriteValue,
): ViewModel() {
    private val _isFavoriteStateFlow = MutableStateFlow<Result<Any>>(Result.Loading())
    val isFavoriteStateFlow: StateFlow<Result<Any>> = _isFavoriteStateFlow

    val pokemonDetail: StateFlow<Result<Pokemon>> =
        getPokemonDetailUseCase(savedStateHandle.get<Int>("id")!!).stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(),
            Result.Loading()
        )

    fun modifyFavoriteValue(pokemonId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            favoriteValueUseCase(pokemonId, isFavorite).collect { r ->
                _isFavoriteStateFlow.value = r
            }
        }
    }
}