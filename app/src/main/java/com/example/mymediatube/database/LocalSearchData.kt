package com.example.mymediatube.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_data")
data class LocalSearchData(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val dataId: String,
    val title: String,
    val thumbnail: String,
    val dataUri: String
)