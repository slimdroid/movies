package com.slimdroid.movies.common

import android.content.Intent
import androidx.core.net.toUri
import com.slimdroid.movies.App

actual fun openMovieTrailer(url: String) {
    val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    App.instance.startActivity(intent)
}