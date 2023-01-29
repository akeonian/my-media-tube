package com.example.mymediatube.repository

import com.example.mymediatube.source.SearchData
import kotlinx.coroutines.flow.Flow

interface DataRepository {

    suspend fun getSearchResults(query: String): List<SearchData>

    suspend fun getHomeData(): List<SearchData>

    suspend fun getSuggestions(keyword: String): List<String>

}