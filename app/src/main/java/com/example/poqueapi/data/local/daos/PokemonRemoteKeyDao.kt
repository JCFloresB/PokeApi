package com.example.poqueapi.data.local.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.poqueapi.data.local.entities.PokemonRemoteKeyEntity

@Dao
interface PokemonRemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKey: PokemonRemoteKeyEntity)

    @Query("SELECT * FROM pokemon_keys_table WHERE id = :id")
    suspend fun getRemoteKeyById(id: String): PokemonRemoteKeyEntity?

    @Query("DELETE FROM pokemon_keys_table WHERE id = :id")
    suspend fun deleteRemoteKeyById(id: String)
}