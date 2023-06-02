package com.example.speaksure_capstone.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.speaksure_capstone.response.ListThreads

@Dao
interface ThreadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertThread(story: List<ListThreads>)

    @Query("SELECT * FROM thread")
    fun getAllThread(): PagingSource<Int, ListThreads>

    @Query("DELETE FROM thread")
    suspend fun deleteAll()
}