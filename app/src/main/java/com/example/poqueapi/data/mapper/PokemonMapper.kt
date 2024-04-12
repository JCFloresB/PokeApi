package com.example.poqueapi.data.mapper

import com.example.poqueapi.data.local.entities.PokemonEntity
import com.example.poqueapi.data.remote.dto.pokemondetail.PokemonDetailResponse
import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.utils.UrlUtils

fun PokemonEntity.toPokemon() = Pokemon(
    pokemonId = pokemonId,
    pokemonName = pokemonName,
    pokemonUrlImage = pokemonUrlImage,
    pokemonHeight = pokemonHeight,
    pokemonWeight = pokemonWeight,
    pokemonTypeList = pokemonTypeList,
    isFavorite = isFavorite,
)

fun Pokemon.toPokemonEntity() = PokemonEntity(
    pokemonId = pokemonId,
    pokemonName = pokemonName,
    pokemonUrlImage = pokemonUrlImage,
    pokemonHeight = pokemonHeight,
    pokemonWeight = pokemonWeight,
    pokemonTypeList = emptyList<String>(),
    isFavorite = isFavorite
)

fun PokemonDetailResponse.toPokemon() = Pokemon(
    pokemonId = id,
    pokemonName = name,
    pokemonUrlImage = UrlUtils.getImageUrl(sprites),
    pokemonHeight = height,
    pokemonWeight = weight,
    pokemonTypeList = types.map { it.type.name } ?: emptyList(),
    isFavorite = false,
)