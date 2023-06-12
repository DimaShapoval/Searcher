package com.onpu.app.util

fun String.findEmailsInText(): List<String> {
    val emailRegex = Regex("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}")
    return emailRegex.findAll(this)
        .map { it.value }
        .toList()
}

