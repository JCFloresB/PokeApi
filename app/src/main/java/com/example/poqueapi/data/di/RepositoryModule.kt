package com.example.poqueapi.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.paging.Pager
import com.example.poqueapi.data.local.PokemonDatabase
import com.example.poqueapi.data.local.entities.PokemonEntity
import com.example.poqueapi.data.remote.PokemonApi
import com.example.poqueapi.data.repository.AuthRepositoryImp
import com.example.poqueapi.data.repository.PokemonRepositoryImp
import com.example.poqueapi.domain.repository.AuthRepository
import com.example.poqueapi.domain.repository.PokemonRepository
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun providePokemonRepository(
        pokemonPager: Pager<Int, PokemonEntity>,
        pokemonDatabase: PokemonDatabase,
        pokemonApi: PokemonApi,
    ): PokemonRepository {
        return PokemonRepositoryImp(
            pokemonPager = pokemonPager,
            pokemonDatabase = pokemonDatabase,
            pokemonApi = pokemonApi,
        )
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth,
        dataStore: DataStore<Preferences>
    ): AuthRepository {
        return AuthRepositoryImp(firebaseAuth = firebaseAuth, store = dataStore)
    }
}