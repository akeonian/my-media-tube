package com.example.mymediatube.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymediatube.ext.asSearchData
import com.example.mymediatube.helper.YoutubeHelper
import com.example.mymediatube.models.SearchData
import com.google.api.client.util.DateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

private const val TAG = "HomeViewModel"

class HomeViewModel: ViewModel() {

    private val _homeData = MutableLiveData<List<SearchData>>()
    private var loadPending = true
    val homeData: LiveData<List<SearchData>> = _homeData
    val isLoadPending get() = loadPending

    fun loadHomeData(pending: Boolean) {
        loadPending = pending
        if (!pending) loadHomeDataFromApi()
    }

    private fun loadHomeDataFromApi() {
        val service = YoutubeHelper.getClient()

        viewModelScope.launch(Dispatchers.IO) {
            val monthAgo = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_MONTH, -30)
            }.time
            val result = YoutubeHelper.execute(service.search()
                .list("id,snippet")
                .setFields("items(id,snippet(title,thumbnails))")
                .setOrder("viewCount")
                .setPublishedAfter(DateTime(monthAgo))
                .setMaxResults(25L)
            )

            val searches = result.items
            val channelInfo = mutableListOf<SearchData>()
            if (searches != null) {
                for (search in searches) {
                    channelInfo.add(search.asSearchData())
                }
            }
            _homeData.postValue(channelInfo)
        }
    }

    fun loadIfPending() = if (loadPending) loadHomeData(false) else Unit
}