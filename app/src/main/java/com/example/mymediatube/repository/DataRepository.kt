package com.example.mymediatube.repository

import com.example.mymediatube.source.SearchData
import kotlinx.coroutines.flow.Flow

interface DataRepository {

    fun getSearchResults(query: String): Flow<List<SearchData>>

    fun getHomeData(): Flow<List<SearchData>>

    fun getSuggestions(keyword: String): Flow<List<String>>

}