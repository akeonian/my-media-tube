package com.example.mymediatube

import android.app.Application
import com.example.mymediatube.database.AppDatabase
import com.example.mymediatube.repository.DataRepository
import com.example.mymediatube.repository.VideoRepository
import com.example.mymediatube.source.LocalDataSource
import com.example.mymediatube.source.YoutubeDataSource

class App: Application() {

    val dataRepository: DataRepository by lazy {

        val database = AppDatabase.getDatabase(this)

        VideoRepository(
            LocalDataSource(database.localDataDao),
            YoutubeDataSource()
        )
    }

}