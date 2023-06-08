package com.example.speaksure_capstone.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.speaksure_capstone.response.ListThreads
import com.example.speaksure_capstone.response.TopicConverter
import com.example.speaksure_capstone.response.UserConverter

@Database(
    entities = [ListThreads::class,RemoteKeys::class],
    version = 5,
    exportSchema = false
)
@TypeConverters(UserConverter::class, TopicConverter::class)
abstract class ThreadDatabase : RoomDatabase(){
    abstract fun threadDao(): ThreadDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object{
        @Volatile
        private var INSTANCE: ThreadDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): ThreadDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ThreadDatabase::class.java, "thread_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}