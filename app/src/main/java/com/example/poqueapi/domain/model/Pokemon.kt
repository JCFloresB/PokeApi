package com.example.poqueapi.domain.model

data class Pokemon(
    val pokemonId: Int,
    val pokemonName: String,
    val pokemonUrlImage: String,
    val pokemonHeight: Int,
    val pokemonWeight: Int,
    val pokemonTypeList: MutableList<String>
)
