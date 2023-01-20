package com.example.mymediatube.repository

import com.example.mymediatube.source.DataSource
import com.example.mymediatube.source.SearchData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VideoRepository(
    private val localDataSource: DataSource, // TODO: Use localDataSource when no internet
    private val remoteDataSource: DataSource
): DataRepository {

    override fun getSearchResults(query: String) = flow {
        val latestSearchResult = remoteDataSource.getSearchResults(query)
        emit(latestSearchResult)
        // TODO: Implement remit logic
    }

    override fun getHomeData() = flow {
        val latestHomeData = remoteDataSource.getHomeData()
        emit(latestHomeData)
        // TODO: Implement remit logic
    }

    override fun getSuggestions(keyword: String) = flow {
        val latestHomeData = remoteDataSource.getSuggestions(keyword)
        emit(latestHomeData)
        // TODO: Implement remit logic
    }

}