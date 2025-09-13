package com.slimdroid.movies.common

import kotlin.Result
import kotlin.coroutines.cancellation.CancellationException

suspend fun <T> runCatchingCancellation(block: suspend () -> T): Result<T> = try {
    Result.Companion.success(block())
} catch (cancellationException: CancellationException) {
    throw cancellationException
} catch (exception: Exception) {
    Result.Companion.failure(exception)
}