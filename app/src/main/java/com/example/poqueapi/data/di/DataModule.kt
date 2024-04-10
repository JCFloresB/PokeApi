package com.example.poqueapi.data.di

import android.content.Context
import androidx.room.Room
import com.example.poqueapi.BuildConfig
import com.example.poqueapi.data.local.PokemonDatabase
import com.example.poqueapi.data.remote.PokemonApi
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
    fun providesPokemonDb(@ApplicationContext context: Context): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            Constants.DB_NAME,
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun providesPokemonApi(): PokemonApi {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }

    @Provides
    @Singleton
    fun providesPokemonPager() {

    }
}