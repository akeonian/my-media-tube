package com.example.mymediatube.repository

import com.example.mymediatube.source.DataSource
import com.example.mymediatube.source.MutableDataSource
import com.example.mymediatube.source.SearchData
import java.net.UnknownHostException

class VideoRepository(
    private val localDataSource: MutableDataSource,
    private val remoteDataSource: DataSource
): DataRepository {


    override suspend fun getSearchResults(query: String): List<SearchData> {
        return try {
            val temp = remoteDataSource.getSearchResults(query)
            localDataSource.clearSearchResults()
            localDataSource.saveSearchResults(temp)
            temp
        } catch (e: UnknownHostException) {
            localDataSource.getSearchResults(query)
        }
    }

    override suspend fun getHomeData(): List<SearchData> {
        return try {
            val temp = remoteDataSource.getHomeData()
            localDataSource.clearHomeData()
            localDataSource.saveHomeData(temp)
            temp
        } catch (e: UnknownHostException) {
            localDataSource.getHomeData()
        }
    }

    override suspend fun getSuggestions(keyword: String): List<String> {
        return remoteDataSource.getSuggestions(keyword)
    }

}