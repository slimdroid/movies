package com.slimdroid.movies.network

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.android.Android

actual fun getHttpEngine(): HttpClientEngine = Android.create()