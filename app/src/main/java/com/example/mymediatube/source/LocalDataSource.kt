package com.example.mymediatube.source

import com.example.mymediatube.database.LocalDataDao
import com.example.mymediatube.database.LocalSearchData

class LocalDataSource(private val localDataDao: LocalDataDao): DataSource {

    override suspend fun getSearchResults(query: String): List<SearchData> {
        // TODO("Not yet implemented")
        return emptyList()
    }

    override suspend fun getHomeData(): List<SearchData> {
        return localDataDao.getAllSearchData().map { it.toSearchData() }
    }

    override suspend fun getSuggestions(keyword: String): List<String> {
        // TODO("Not yet implemented")
        return emptyList()
    }

    private fun LocalSearchData.toSearchData(): SearchData {
        return SearchData(dataId, title, thumbnail, dataUri)
    }
}