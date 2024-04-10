package com.example.poqueapi.data.remote.dto.pokemondetail


import com.google.gson.annotations.SerializedName

data class Cries(
    @SerializedName("latest")
    val latest: String,
    @SerializedName("legacy")
    val legacy: String
)