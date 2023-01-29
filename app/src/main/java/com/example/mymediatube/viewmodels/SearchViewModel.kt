package com.example.mymediatube.viewmodels

import androidx.lifecycle.*
import com.example.mymediatube.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(
    private val dataRepository: DataRepository
): ViewModel() {

    private val smallSearchDelay = 1000L
    private val _suggestions = MutableLiveData<List<String>>()
    val suggestions: LiveData<List<String>> = _suggestions
    private var pendingKeyword: String? = null

    private var job: Job? = null

    private fun searchSuggestions(keyword: String) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            // If the keyword is too small wait for user to type more
            if (keyword.length < 4) delay(smallSearchDelay)
            val list = dataRepository.getSuggestions(keyword)
            _suggestions.postValue(list)
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
    
        class Factory(private val dataRepository: DataRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel(dataRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}