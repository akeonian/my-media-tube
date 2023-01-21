package com.example.mymediatube.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LocalSearchData::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract val localDataDao: LocalDataDao

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build().apply {
                        INSTANCE = this
                    }
            }
        }
    }
}