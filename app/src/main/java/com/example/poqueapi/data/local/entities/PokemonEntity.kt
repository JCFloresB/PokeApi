package com.example.poqueapi.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.poqueapi.utils.Constants.POKEMON_TABLE_NAME

@Entity(tableName = POKEMON_TABLE_NAME)
data class PokemonEntity(
    @PrimaryKey val pokemonId: Int = 0,
    val pokemonName: String,
    val pokemonUrlImage: String,
    val pokemonHeight: Int,
    val pokemonWeight: Int,
    val pokemonTypeList: List<String>,
    val isFavorite: Boolean,
)
