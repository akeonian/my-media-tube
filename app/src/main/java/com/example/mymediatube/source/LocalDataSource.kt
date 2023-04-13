package com.example.mymediatube.source

import com.example.mymediatube.database.LocalDataDao
import com.example.mymediatube.database.LocalHomeData
import com.example.mymediatube.database.LocalSearchData

class LocalDataSource(private val localDataDao: LocalDataDao): MutableDataSource {

    override suspend fun getSearchResults(query: String): List<SearchData> {
        return localDataDao.getAllSearchData().map { it.toSearchData() }
    }

    override suspend fun saveSearchResults(data: List<SearchData>) {
        return localDataDao.insertAllSearchData(data.map { it.asLocalSearchData() })
    }

    override suspend fun clearSearchResults() {
        localDataDao.deleteAllSearchData()
    }

    override suspend fun getHomeData(): List<SearchData> {
        return localDataDao.getAllHomeData().map { it.toSearchData() }
    }

    override suspend fun getSuggestions(keyword: String): List<String> {
        // TODO("Not yet implemented")
        return emptyList()
    }

    override suspend fun saveHomeData(data: List<SearchData>) {
        localDataDao.insertAllHomeData(data.map { it.asLocalHomeData() })
    }

    override suspend fun clearHomeData() {
        localDataDao.deleteAllHomeData()
    }

    private fun LocalSearchData.toSearchData(): SearchData {
        return SearchData(dataId, title, thumbnail, dataUri)
    }

    private fun LocalHomeData.toSearchData(): SearchData {
        return SearchData(dataId, title, thumbnail, dataUri)
    }
}