package com.example.poqueapi.presentation.pokemon.list.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.poqueapi.R
import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.presentation.theme.PoqueApiTheme

@Composable
fun PokemonItem(
    pokemon: Pokemon,
    onClickItem: () -> Unit,
    modifier: Modifier = Modifier
    ) {
    Column(modifier = modifier
        .clickable { onClickItem() }
        .padding(all = 5.dp)
        .fillMaxWidth()
        ) {
        AsyncImage(
            model = pokemon.pokemonUrlImage,
            contentDescription = "Imagen principal de pokemon",
            modifier = Modifier.size(150.dp),
            contentScale = ContentScale.Fit,
            placeholder = painterResource(R.drawable.image_placeholder_icon)

        )

        Text(
            text = pokemon.pokemonName,
            Modifier.padding(4.dp)
        )
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
                pokemonTypeList = mutableListOf("grass", "poisson")
            ), onClickItem = {  })
        }
    }
}