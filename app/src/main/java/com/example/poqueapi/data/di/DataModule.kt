package com.example.poqueapi.data.di

import android.content.Context
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.room.Room
import com.example.poqueapi.BuildConfig
import com.example.poqueapi.data.local.PokemonDatabase
import com.example.poqueapi.data.local.entities.PokemonEntity
import com.example.poqueapi.data.remote.PokemonApi
import com.example.poqueapi.data.remote.PokemonRemoteMediator
import com.example.poqueapi.data.repository.PokemonRepositoryImp
import com.example.poqueapi.domain.repository.PokemonRepository
import com.example.poqueapi.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {
    @Provides
    @Singleton
    fun providePokemonDb(@ApplicationContext context: Context): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            Constants.DB_NAME,
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providePokemonApi(): PokemonApi {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }

    @OptIn(ExperimentalPagingApi::class)
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
}