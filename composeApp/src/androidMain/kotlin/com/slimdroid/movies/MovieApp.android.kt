package com.slimdroid.movies

import android.util.Log

actual fun logInfo(tag: String, message: String) {
    Log.i(tag, message)
}

actual fun logError(tag: String, message: String, throwable: Throwable?) {
    Log.e(tag, message, throwable)
}