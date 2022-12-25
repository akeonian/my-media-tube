package com.example.mymediatube.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymediatube.ext.asSearchData
import com.example.mymediatube.helper.YoutubeHelper
import com.example.mymediatube.models.SearchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchResultViewModel: ViewModel() {

    private val _searchData = MutableLiveData<List<SearchData>>()
    val searchData: LiveData<List<SearchData>> = _searchData

    private var loadPending = false
    private var lastQuery: String? = null
    private var searchJob: Job? = null

    fun performSearch(pending: Boolean, query: String) {
        loadPending = pending
        if (query != lastQuery) {
            lastQuery = query
            if (!pending) {
                performSearch(query)
            }
        }
    }

    private fun performSearch(query: String) {
        searchJob?.cancel()
        val client = YoutubeHelper.getClient()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            val result = YoutubeHelper.execute(
                client.search()
                    .list("id,snippet")
                    .setFields("items(id,snippet(title,thumbnails))")
                    .setQ(query)
                    .setMaxResults(25L)
            )

            val searches = result.items
            val newSearch = mutableListOf<SearchData>()
            for (search in searches) {
                newSearch.add(search.asSearchData())
            }
            _searchData.postValue(newSearch)
        }
    }

    fun loadIfPending() = lastQuery?.let {
        performSearch(it)
        loadPending = false
    }
}