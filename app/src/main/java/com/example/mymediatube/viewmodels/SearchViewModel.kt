package com.example.mymediatube.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymediatube.helper.YoutubeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel: ViewModel() {

    private val maxSuggestions = 6L
    private val smallSearchDelay = 1000L
    private val _suggestions = MutableLiveData<List<String>>()
    val suggestions: LiveData<List<String>> = _suggestions
    private var pendingKeyword: String? = null

    private var job: Job? = null

    private fun searchSuggestions(keyword: String) {
        job?.cancel()
        val client = YoutubeHelper.getClient()
        job = viewModelScope.launch(Dispatchers.IO) {
            // If the keyword is too small wait for user to type more
            if (keyword.length < 4) delay(smallSearchDelay)

            // TODO: change suggestion search strategy
            val result = YoutubeHelper.execute(
                client.search()
                    .list("snippet")
                    .setFields("items(snippet/title)")
                    .setQ(keyword)
                    .setMaxResults(maxSuggestions)
            )

            val newSuggestions = mutableListOf<String>()
            val results = result.items
            for (r in results) {
                newSuggestions.add(r.snippet.title)
            }
            _suggestions.postValue(newSuggestions)
        }
    }

    fun searchSuggestions(pending: Boolean, keyword: String) {
        if (pending) {
            pendingKeyword = keyword
        } else {
            pendingKeyword = null
            searchSuggestions(keyword)
        }
    }

    fun loadIfPending() = pendingKeyword?.let {
        searchSuggestions(it)
        pendingKeyword = null
    }
}