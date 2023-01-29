package com.example.mymediatube.viewmodels

import androidx.lifecycle.*
import com.example.mymediatube.models.UISearchData
import com.example.mymediatube.repository.DataRepository
import com.example.mymediatube.source.asUIDataList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "HomeViewModel"

class HomeViewModel(
    private val dataRepository: DataRepository
): ViewModel() {

    private val _homeData = MutableLiveData<List<UISearchData>>()
    private val isLoading = MutableLiveData(false)
    val showRefreshing: LiveData<Boolean> = isLoading
    val homeData: LiveData<List<UISearchData>> = _homeData

    private var alreadyLoaded = false

    // Should not load until loadHomeData is called
    fun loadHomeData() {
        loadHomeDataFromApi()
    }

    private fun loadHomeDataFromApi() {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val list = dataRepository.getHomeData().asUIDataList()
            _homeData.postValue(list)
            isLoading.postValue(false)
        }
    }

    fun loadIfNotLoaded() {
        if (isLoading.value == false && !alreadyLoaded) loadHomeData()
    }

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