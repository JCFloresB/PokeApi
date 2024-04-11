package com.example.poqueapi.utils

import timber.log.Timber

object KeyUtils {

    fun getNextKey(nextUrl: String): Int {
        var url = nextUrl
        val invalidOffset = 0

        if(url.isBlank()) return invalidOffset

        val ampSingPosition = url.lastIndexOf('&')
        url = url.removeRange(ampSingPosition, url.length)

        val questionSignPosition = url.lastIndexOf('?')
        Timber.i("Posision de ?: $questionSignPosition")
        val offsetData = url.substring(questionSignPosition + 1, url.length).replace("offset=", "")
        Timber.i("Sub string: $offsetData")
        return try {
            offsetData.toInt()
        } catch(e: NumberFormatException) {
            0
        }
    }
}