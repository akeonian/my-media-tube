package com.example.mymediatube.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymediatube.BuildConfig
import com.example.mymediatube.DEBUG_SHA
import com.example.mymediatube.YOUTUBE_KEY
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.youtube.YouTube
import com.google.api.services.youtube.YouTubeRequestInitializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TestViewModel: ViewModel() {

    private val _channelData = MutableLiveData<List<String>>(emptyList())
    val channelData: LiveData<List<String>> = _channelData

    private val _searchData = MutableLiveData<List<String>>(emptyList())
    val searchData: LiveData<List<String>> = _searchData

    fun loadChannelDataFromApi(credential: GoogleAccountCredential) {
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        val service = YouTube.Builder(
            transport, jsonFactory, credential)
            .setApplicationName("YouTube Data API")
            .build()

        viewModelScope.launch(Dispatchers.IO) {
            val channelInfo = mutableListOf<String>()
            val result = service.channels()
                .list("snippet,contentDetails,statistics")
                .setForUsername("GoogleDevelopers")
                .execute()
            val channels = result.items
            if (channels != null) {
                val channel = channels[0]
                channelInfo.add("This channel's ID is ${channel.id}. " +
                    "Its title is '${channel.snippet.title}, " +
                    "and it has ${channel.statistics.viewCount} views." )
            }
            _channelData.value = channelInfo
        }
    }

    fun loadSearchDataFromApi() {
        val transport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = JacksonFactory.getDefaultInstance()
        val service = YouTube.Builder(
            transport, jsonFactory, null)
            .setApplicationName("YouTube Data API")
            .setYouTubeRequestInitializer(YouTubeRequestInitializer(YOUTUBE_KEY))
            .build()

        viewModelScope.launch(Dispatchers.IO) {
            val searchInfo = mutableListOf<String>()
            val result = service.search()
                .list("snippet")
                .setMaxResults(25L)
                .setQ("Surfing")
                .apply {
                    requestHeaders.set(
                        "X-Android-Package",
                        BuildConfig.APPLICATION_ID
                    ).set("X-Android-Cert", DEBUG_SHA)
                }
                .execute()
            val searches = result.items
            if (searches != null) {
                for (search in searches) {
                    searchInfo.add(search.snippet.title)
                }
            }
            _searchData.postValue(searchInfo)
        }
    }
}