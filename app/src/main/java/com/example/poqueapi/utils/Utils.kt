package com.example.poqueapi.utils

import java.util.Locale


fun String.getNameInitials(): String {
    val parts = this.uppercase(Locale.US).replace("-", " ").split(" ")
    return if (parts.size > 1) {
        parts[0][0].toString() + parts[1][0].toString()
    } else {
        parts[0][0].toString()
    }
}