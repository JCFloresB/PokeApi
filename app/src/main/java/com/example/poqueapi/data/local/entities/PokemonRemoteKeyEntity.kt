package com.example.poqueapi.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.poqueapi.utils.Constants

@Entity(tableName = Constants.POKEMON_KEYS_TABLE)
data class PokemonRemoteKeyEntity(
    @PrimaryKey val id: String,
    val nextOffset: Int
)
