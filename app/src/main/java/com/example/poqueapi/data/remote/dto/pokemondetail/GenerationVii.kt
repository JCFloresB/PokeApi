package com.example.poqueapi.data.remote.dto.pokemondetail


import com.google.gson.annotations.SerializedName

data class GenerationVii(
    @SerializedName("icons")
    val icons: Icons,
    @SerializedName("ultra-sun-ultra-moon")
    val ultraSunUltraMoon: UltraSunUltraMoon
)