package com.example.poqueapi.presentation.pokemon.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.presentation.pokemon.list.components.PokemonItem
import timber.log.Timber

@Composable
fun PokemonListScreen(
    navigateToDetail: (Int) -> Unit,
    snackbarHostState: SnackbarHostState,
) {

    val viewModel = hiltViewModel<PokemonListViewModel>()
    val pokemonPagingItems = viewModel.pokemonPagingDataFlow.collectAsLazyPagingItems()

    if (pokemonPagingItems.loadState.refresh is LoadState.Error) {
        LaunchedEffect(key1 = snackbarHostState) {
            snackbarHostState.showSnackbar(
                (pokemonPagingItems.loadState.refresh as LoadState.Error).error.message ?: ""
            )
        }
    }

    PokemonListContent(
        pokemonPagingItems = pokemonPagingItems,
        navigateToDetail = navigateToDetail,
    )

}

@Composable
fun PokemonListContent(
    pokemonPagingItems: LazyPagingItems<Pokemon>,
    navigateToDetail: (Int) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (pokemonPagingItems.loadState.refresh is LoadState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(3)
            ) {
                items(
                    count = pokemonPagingItems.itemCount,
                    key = pokemonPagingItems.itemKey { it.pokemonId },
                ) { index ->
                    val pokemon = pokemonPagingItems[index]
                    if (pokemon != null) {
                        PokemonItem(
                            pokemon,
                            onClickItem = {
                                Timber.d("Click en item de pokemon: ${pokemon.pokemonName}")
                                navigateToDetail(pokemon.pokemonId)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
                item {
                    if (pokemonPagingItems.loadState.append is LoadState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }
}