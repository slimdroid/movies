package com.slimdroid.movies.common

import co.touchlab.kermit.Logger
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun openMovieTrailer(url: String) {
    val nsUrl = NSURL(string = url) ?: return

    if (url.contains("youtube.com/watch?v=")) {
        tryOpenYouTubeApp(url)
    } else {
        openInBrowser(nsUrl)
    }
}

private fun openInBrowser(url: NSURL) {
    UIApplication.sharedApplication.openURL(
        url = url,
        options = emptyMap<Any?, Any>(),
        completionHandler = { success: Boolean ->
            if (success.not()) {
                Logger.w { "❌ Cannot open URL: $url" }
            }
        }
    )
}

private fun tryOpenYouTubeApp(url: String) {
    val videoId = url
        .substringAfter("v=", missingDelimiterValue = "")
        .substringBefore("&")

    NSURL(string = "youtube://$videoId")?.let { youtubeUrl ->
        UIApplication.sharedApplication.openURL(
            youtubeUrl,
            options = emptyMap<Any?, Any>(),
            completionHandler = { success ->
                if (success.not()) {
                    // If YouTube doesn't exist → open browser
                    openInBrowser(NSURL(string = "https://www.youtube.com/watch?v=$videoId")!!)
                }
            }
        )
    }
}