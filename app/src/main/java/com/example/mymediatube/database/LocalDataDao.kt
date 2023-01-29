package com.example.mymediatube.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface LocalDataDao {

    @Query("SELECT * FROM search_data")
    suspend fun getAllSearchData(): List<LocalSearchData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSearchData(data: List<LocalSearchData>)

    @Query("DELETE FROM search_data")
    suspend fun deleteAllSearchData()

    @Query("SELECT * FROM home_data")
    suspend fun getAllHomeData(): List<LocalHomeData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllHomeData(data: List<LocalHomeData>)

    @Query("DELETE FROM home_data")
    suspend fun deleteAllHomeData()
}