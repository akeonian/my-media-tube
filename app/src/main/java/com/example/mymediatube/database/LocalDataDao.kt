package com.example.mymediatube.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface LocalDataDao {

    @Query("SELECT * From search_data")
    suspend fun getAllSearchData(): List<LocalSearchData>

}