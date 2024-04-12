package com.example.poqueapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.poqueapi.data.local.daos.PokemonDao
import com.example.poqueapi.data.local.daos.PokemonRemoteKeyDao
import com.example.poqueapi.data.local.entities.PokemonEntity
import com.example.poqueapi.data.local.entities.PokemonRemoteKeyEntity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Database(
    entities = [PokemonEntity::class, PokemonRemoteKeyEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PokemonDatabase: RoomDatabase() {
    abstract val pokemonDao: PokemonDao
    abstract val remoteKeyDao: PokemonRemoteKeyDao
}

class Converters {

    @TypeConverter
    fun fromStringList(value: List<String>) = Json.encodeToString(value)

    @TypeConverter
    fun toStringList(value: String) = Json.decodeFromString<List<String>>(value)

}