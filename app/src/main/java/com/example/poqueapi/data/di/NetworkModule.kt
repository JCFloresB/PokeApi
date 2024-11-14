package com.example.poqueapi.data.di

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.poqueapi.BuildConfig
import com.example.poqueapi.data.local.PokemonDatabase
import com.example.poqueapi.data.local.entities.PokemonEntity
import com.example.poqueapi.data.remote.PokemonApi
import com.example.poqueapi.data.remote.PokemonRemoteMediator
import com.example.poqueapi.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun providePokemonApi(okHttpClient: OkHttpClient): PokemonApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonPager(
        pokemonDatabase: PokemonDatabase,
        pokemonApi: PokemonApi
    ): Pager<Int, PokemonEntity> {
        return Pager(
            config = PagingConfig(pageSize = Constants.POKEMON_PAGING_PAGE_SIZE),
            remoteMediator = PokemonRemoteMediator(
                pokemonDatabase = pokemonDatabase,
                pokemonApi = pokemonApi,
            ),
            pagingSourceFactory = {
                pokemonDatabase.pokemonDao.pagingSource()
            }
        )
    }
}