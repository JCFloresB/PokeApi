package com.example.poqueapi.presentation.pokemon.list.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.poqueapi.R
import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.presentation.theme.PoqueApiTheme
import com.example.poqueapi.utils.getNameInitials

@Composable
fun PokemonItem(
    pokemon: Pokemon,
    onClickItem: () -> Unit,
    onClickFavoriteItem: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier
        .clickable { onClickItem() }
        .padding(all = 4.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PokemonImage(pokemon = pokemon)

        Row {
            Text(
                text = pokemon.pokemonName,
                Modifier.padding(4.dp),
                fontSize = 16.sp
            )
            if (pokemon.isFavorite) {
                Icon(
                    Icons.Filled.Favorite,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onClickFavoriteItem()
                    },
                    tint = Color.Red
                )
            } else {
                Icon(
                    Icons.Filled.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onClickFavoriteItem()
                    },
                    tint = Color.Red
                )
            }

        }
    }
}

@Composable
fun PokemonImage(pokemon: Pokemon) {
    val painter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(LocalContext.current)
            .data(pokemon.pokemonUrlImage)
            .size(Size.ORIGINAL)
            .placeholder(R.drawable.image_placeholder_icon)
            .build()
    )

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(Color(0xFF8998BA))
    ) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(26.dp)
                )
            }

            is AsyncImagePainter.State.Error -> {
                Text(
                    text = pokemon.pokemonName.getNameInitials(),
                    fontSize = 50.sp,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(24.dp)
                )
            }

            else -> {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(2.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PokemonItemPreview() {
    PoqueApiTheme {
        Surface {
            PokemonItem(pokemon = Pokemon(
                pokemonId = 1,
                pokemonName = "Picachu",
                pokemonUrlImage = "",
                pokemonWeight = 150,
                pokemonHeight = 100,
                pokemonTypeList = mutableListOf("grass", "poisson"),
                isFavorite = false
            ), onClickItem = { },
                onClickFavoriteItem = { })
        }
    }
}