package com.example.mymediatube.ext

import android.net.Uri
import com.example.mymediatube.models.SearchData
import com.google.api.services.youtube.model.SearchResult


// TODO: change the implementation for SearchData
fun SearchResult.asSearchData() = SearchData(
    id = id.toString(),
    title = snippet.title,
    thumbnail = Uri.parse(snippet.thumbnails.medium.url)
)