package com.example.mymediatube.viewmodels

import androidx.lifecycle.*
import com.example.mymediatube.models.UISearchData
import com.example.mymediatube.repository.DataRepository
import com.example.mymediatube.source.asUIData
import com.example.mymediatube.source.asUIDataList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class SearchResultViewModel(
    private val dataRepository: DataRepository
): ViewModel() {

    private val isLoading = MutableLiveData (false)
    private val _searchData = MutableLiveData<List<UISearchData>>()
    val searchData: LiveData<List<UISearchData>> = _searchData
    val showRefreshing: LiveData<Boolean> = isLoading

    private var lastQuery: String? = null
    private var searchJob: Job? = null

    // search when passed with query
    // if searching then
    // cancel current
    // if reloaded then load with last query
    //

    fun performSearch(query: String? = null) {
        if (query == null) {
            lastQuery?.let {
                if (it.isNotEmpty()) performSearch(it)
            }
        } else if (query != lastQuery) {
            lastQuery = query
            searchJob?.cancel()
            isLoading.value = true
            searchJob = viewModelScope.launch(Dispatchers.IO) {
                val list = dataRepository.getSearchResults(query).asUIDataList()
                _searchData.postValue(list)
                isLoading.value = false
            }
        }
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