package com.example.poqueapi.data.local.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.poqueapi.data.local.entities.PokemonEntity
import com.example.poqueapi.domain.model.Pokemon
import com.example.poqueapi.utils.Constants

@Dao
interface PokemonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(pokemons: List<PokemonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pokemon: PokemonEntity)

    @Query("SELECT * FROM ${Constants.POKEMON_TABLE_NAME} WHERE pokemonId = :id")
    suspend fun getPokemonById(id: Int): Pokemon?

    @Query("DELETE FROM pokemon")
    suspend fun clearPokemons()

    @Query("SELECT * FROM pokemon")
    fun pagingSource(): PagingSource<Int, PokemonEntity>
}