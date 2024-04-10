package com.example.poqueapi.utils

object Constants {
    //region DB
    const val DB_NAME = "PokeApi.db"
    const val POKEMON_TABLE_NAME = "pokemon"
    const val POKEMON_KEYS_TABLE = "pokemon_keys_table"
    //endregion

    //region PAGING
    const val POKEMON_PAGING_STARTING_KEY = 0
    const val POKEMON_PAGING_PAGE_SIZE = 25
    const val POKEMON_PAGING_PREFETCH_DISTANCE = 30
    const val POKEMON_PAGING_MAX_SIZE = 150
    //endregion
}