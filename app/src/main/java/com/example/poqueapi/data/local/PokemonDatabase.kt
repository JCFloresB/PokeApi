package com.example.poqueapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.poqueapi.data.local.daos.PokemonDao
import com.example.poqueapi.data.local.entities.PokemonEntity

@Database(
    entities = [PokemonEntity::class],
    version = 1,
    exportSchema = false
)
abstract class PokemonDatabase: RoomDatabase() {
    abstract val pokemonDao: PokemonDao
}