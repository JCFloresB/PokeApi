package com.example.poqueapi.data.remote.dto.pokemondetail


import com.google.gson.annotations.SerializedName

data class GenerationV(
    @SerializedName("black-white")
    val blackWhite: BlackWhite
)