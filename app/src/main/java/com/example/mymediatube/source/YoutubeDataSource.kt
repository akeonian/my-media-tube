package com.example.mymediatube.source

import com.example.mymediatube.ext.asSearchData
import com.example.mymediatube.helper.YoutubeHelper
import com.google.api.client.util.DateTime
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class YoutubeDataSource(
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
): DataSource {

    override suspend fun getSearchResults(query: String): List<SearchData> = withContext(ioDispatcher) {
        val client = YoutubeHelper.getClient()
        val result = YoutubeHelper.execute(
            client.search()
                .list("id,snippet")
                .setFields("items(id,snippet(title,thumbnails))")
                .setQ(query)
                .setMaxResults(25L)
        )

        val searches = result.items
        val newSearch = mutableListOf<SearchData>()
        for (search in searches) {
            newSearch.add(search.asSearchData())
        }
        newSearch
    }

    override suspend fun getHomeData(): List<SearchData> = withContext(ioDispatcher) {
        val monthAgo = Calendar.getInstance().apply {
            add(Calendar.DAY_OF_MONTH, -30)
        }.time
        val client = YoutubeHelper.getClient()
        val result = YoutubeHelper.execute(client.search()
            .list("id,snippet")
            .setFields("items(id,snippet(title,thumbnails))")
            .setOrder("viewCount")
            .setPublishedAfter(DateTime(monthAgo))
            .setMaxResults(25L)
        )

        val searches = result.items
        val channelInfo = mutableListOf<SearchData>()
        if (searches != null) {
            for (search in searches) {
                channelInfo.add(search.asSearchData())
            }
        }
        channelInfo
    }

    override suspend fun getSuggestions(keyword: String): List<String> = withContext(ioDispatcher) {
        // TODO: change suggestion search strategy
        val client = YoutubeHelper.getClient()
        val result = YoutubeHelper.execute(
            client.search()
                .list("snippet")
                .setFields("items(snippet/title)")
                .setQ(keyword)
                .setMaxResults(MAX_SUGGESTIONS)
        )

        val newSuggestions = mutableListOf<String>()
        val results = result.items
        for (r in results) {
            newSuggestions.add(r.snippet.title)
        }
        newSuggestions
    }

    companion object {
        private const val MAX_SUGGESTIONS = 6L
    }
}