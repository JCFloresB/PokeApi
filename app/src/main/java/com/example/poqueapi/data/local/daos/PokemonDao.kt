package com.example.poqueapi.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.poqueapi.data.local.entities.PokemonEntity
import com.example.poqueapi.utils.Constants

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<PokemonEntity>)

    @Query("SELECT * FROM ${Constants.POKEMON_TABLE_NAME} WHERE pokemonId = :id")
    suspend fun getPokemonById(id: Int): PokemonEntity

    @Query("DELETE FROM pokemon")
    suspend fun clearPokemons()
}