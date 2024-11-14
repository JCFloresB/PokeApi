package com.example.poqueapi.data.di

import android.content.Context
import androidx.room.Room
import com.example.poqueapi.data.local.PokemonDatabase
import com.example.poqueapi.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PokemonDbModule {

    @Provides
    @Singleton
    fun providePokemonDb(@ApplicationContext context: Context): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            Constants.DB_NAME,
        ).fallbackToDestructiveMigration().build()
    }
}