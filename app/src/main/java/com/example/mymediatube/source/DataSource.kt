package com.example.mymediatube.source

import android.net.Uri
import com.example.mymediatube.models.UISearchData

interface DataSource {

    suspend fun getSearchResults(query: String): List<SearchData>

    suspend fun getHomeData(): List<SearchData>

    suspend fun getSuggestions(keyword: String): List<String>
}

data class SearchData(
    val id: String,
    val title: String,
    val thumbnail: String,
    val dataUri: String
)

fun SearchData.asUIData() = UISearchData(
    id = id,
    title = title,
    thumbnail = Uri.parse(thumbnail)
)

fun List<SearchData>.asUIDataList() = map { it.asUIData() }
