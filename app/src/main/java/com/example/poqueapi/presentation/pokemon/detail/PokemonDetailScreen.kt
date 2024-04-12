package com.example.poqueapi.presentation.pokemon.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.domain.model.Result
import com.example.poqueapi.utils.getNameInitials
import timber.log.Timber

@Composable
fun PokemonDetailScreen(
    snackbarHostState: SnackbarHostState
) {
    val viewModel = hiltViewModel<PokemonDetailViewModel>()
    val pokemonDetail by viewModel.pokemonDetail.collectAsStateWithLifecycle()
    val favorite by viewModel.isFavoriteStateFlow.collectAsStateWithLifecycle()

    if (pokemonDetail is Result.Error) {
        LaunchedEffect(key1 = snackbarHostState) {
            snackbarHostState.showSnackbar((pokemonDetail as Result.Error).error)
        }
    }

    when (favorite) {
        is Result.Error -> {
            Timber.e(
                "Error: ${
                    (favorite
                            as Result.Error<Any>).error
                }"
            )
        }

        is Result.Loading -> Timber.i("Loading")
        is Result.Success -> Timber.d("Se guardo como favorito: ${favorite.data}")
    }

    PokemonDetailContent(pokemonResponse = pokemonDetail, viewModel)
}

@Composable
fun PokemonDetailContent(
    pokemonResponse: Result<Pokemon>,
    viewModel: PokemonDetailViewModel,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (pokemonResponse.data != null) {
            val pokemon = pokemonResponse.data!!
            var isFavorite by remember {
                mutableStateOf(pokemon.isFavorite)
            }

            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Header(pokemon = pokemon)
                Spacer(modifier = Modifier.width(20.dp))
                if (isFavorite) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                viewModel.modifyFavoriteValue(
                                    pokemonId = pokemon.pokemonId,
                                    isFavorite = !isFavorite
                                )
                                isFavorite = false
                            }
                            .size(50.dp),
                        tint = Color.Red
                    )
                } else {
                    Icon(
                        Icons.Filled.FavoriteBorder,
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                viewModel.modifyFavoriteValue(
                                    pokemonId = pokemon.pokemonId,
                                    isFavorite = !isFavorite
                                )
                                isFavorite = true
                            }
                            .size(50.dp),
                        tint = Color.Red,
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Footer(pokemon = pokemon)

            }
        }
        if (pokemonResponse is Result.Loading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun Header(pokemon: Pokemon) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(pokemon.pokemonUrlImage)
            .size(Size.ORIGINAL)
            .build()
    )
    val imageSize = 250.dp

    Box(
        modifier = Modifier
            .size(imageSize)
            .clip(CircleShape)
            .background(Color(0xFF8998BA))
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is AsyncImagePainter.State.Error -> {
                Text(
                    text = pokemon.pokemonName.getNameInitials(),
                    fontSize = 50.sp,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

            else -> {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun Footer(pokemon: Pokemon) {
    Column(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            pokemon.pokemonName,
            style = MaterialTheme.typography.titleLarge,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Weight: ${pokemon.pokemonWeight}")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Height: ${pokemon.pokemonHeight}")
        Spacer(modifier = Modifier.height(12.dp))
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Tipo: ")
            pokemon.pokemonTypeList.mapIndexed { index, type ->
                Text(type)
                if (index < pokemon.pokemonTypeList.lastIndex) Text("•")
            }
        }
    }
}