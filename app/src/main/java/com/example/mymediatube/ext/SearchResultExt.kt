package com.example.mymediatube.ext

import com.example.mymediatube.source.SearchData
import com.google.api.services.youtube.model.SearchResult


// TODO: change the implementation for SearchData
fun SearchResult.asSearchData() = SearchData(
    id = id.toString(),
    title = snippet.title,
    thumbnail = snippet.thumbnails.medium.url,
    dataUri = ""
)