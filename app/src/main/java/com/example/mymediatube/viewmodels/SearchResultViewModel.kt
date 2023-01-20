package com.example.mymediatube.viewmodels

import androidx.lifecycle.*
import com.example.mymediatube.models.UISearchData
import com.example.mymediatube.repository.DataRepository
import com.example.mymediatube.source.asUIDataList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchResultViewModel(
    private val dataRepository: DataRepository
): ViewModel() {

    private val _searchData = MutableLiveData<List<UISearchData>>()
    val searchData: LiveData<List<UISearchData>> = _searchData

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
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            dataRepository.getSearchResults(query)
                .map { it.asUIDataList() }
                .collect {
                    _searchData.postValue(it)
                }
        }
    }

    fun loadIfPending() = lastQuery?.let {
        performSearch(it)
        loadPending = false
    }

    class Factory(private val dataRepository: DataRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchResultViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SearchResultViewModel(dataRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}