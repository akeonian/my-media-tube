package com.example.mymediatube.viewmodels

import androidx.lifecycle.*
import com.example.mymediatube.models.UISearchData
import com.example.mymediatube.repository.DataRepository
import com.example.mymediatube.source.asUIDataList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel(
    private val dataRepository: DataRepository
): ViewModel() {

    private val _homeData = MutableLiveData<List<UISearchData>>()
    private var loadPending = true
    val homeData: LiveData<List<UISearchData>> = _homeData
    val isLoadPending get() = loadPending

    // Should not load until loadHomeData is called
    fun loadHomeData(pending: Boolean) {
        loadPending = pending
        if (!pending) loadHomeDataFromApi()
    }

    private fun loadHomeDataFromApi() {
        viewModelScope.launch(Dispatchers.IO) {
            dataRepository.getHomeData()
                .map { it.asUIDataList() }
                .collect {
                _homeData.postValue(it)
            }
        }
    }

    fun loadIfPending() = if (loadPending) loadHomeData(false) else Unit

    class Factory(private val dataRepository: DataRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(dataRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}