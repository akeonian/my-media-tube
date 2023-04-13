package com.example.mymediatube.helper

import com.example.mymediatube.BuildConfig
import com.example.mymediatube.DEBUG_SHA
import com.example.mymediatube.YOUTUBE_KEY
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeRequest
import com.google.api.services.youtube.YouTubeRequestInitializer

object YoutubeHelper {
    private const val APP_NAME = "YouTube Data API"
    fun getClient(): YouTube {
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        return YouTube.Builder(
            transport, jsonFactory, null)
            .setApplicationName(APP_NAME)
            .setYouTubeRequestInitializer(YouTubeRequestInitializer(YOUTUBE_KEY))
            .build()
    }

    fun <T> execute(request: YouTubeRequest<T>): T {
        request.requestHeaders.set(
            "X-Android-Package",
            BuildConfig.APPLICATION_ID
        ).set("X-Android-Cert", DEBUG_SHA)
        return request.execute()
    }

    fun getWatchUrl(videoId: String): String {
        return "https://www.youtube.com/watch?v=$videoId"
    }
}