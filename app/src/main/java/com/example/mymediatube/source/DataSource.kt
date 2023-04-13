package com.example.mymediatube.source

import android.net.Uri
import com.example.mymediatube.database.LocalHomeData
import com.example.mymediatube.database.LocalSearchData
import com.example.mymediatube.models.UISearchData

interface DataSource {

    suspend fun getSearchResults(query: String): List<SearchData>
    suspend fun getHomeData(): List<SearchData>

    suspend fun getSuggestions(keyword: String): List<String>
}

interface MutableDataSource: DataSource {

    suspend fun saveSearchResults(data: List<SearchData>)
    suspend fun clearSearchResults()

    suspend fun saveHomeData(data: List<SearchData>)
    suspend fun clearHomeData()

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
    thumbnail = Uri.parse(thumbnail),
    dataUri = Uri.parse(dataUri)
)

fun SearchData.asLocalHomeData() = LocalHomeData(
    dataId = id,
    title = title,
    thumbnail = thumbnail,
    dataUri = dataUri
)

fun SearchData.asLocalSearchData() = LocalSearchData(
    dataId = id,
    title = title,
    thumbnail = thumbnail,
    dataUri = dataUri
)

fun List<SearchData>.asUIDataList() = map { it.asUIData() }
