package com.slimdroid.movies

actual fun logInfo(tag: String, message: String) {
    println("tag: $tag, message: $message")
}

actual fun logError(tag: String, message: String, throwable: Throwable?) {
    println("tag: $tag, message: $message, throwable: $throwable")
}