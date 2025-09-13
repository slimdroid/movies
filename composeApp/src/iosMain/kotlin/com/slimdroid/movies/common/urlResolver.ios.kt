package com.slimdroid.movies.common

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openMovieTrailer(url: String) {
    // FIXME doesn't work on simulator
    val nsUrl = NSURL(string = url)
    nsUrl?.let {
        if (UIApplication.sharedApplication.canOpenURL(it)) {
            UIApplication.sharedApplication.openURL(it)
        } else {
            println("Cannot open URL: $url")
        }
    }
}