package com.hausberger.mvistarter.framework.datasource.cache.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.hausberger.mvistarter.framework.datasource.cache.model.SampleCacheEntity

@Dao
interface SampleDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(sample: SampleCacheEntity): Long

    @Query("SELECT * FROM samples")
    suspend fun getAllSamples(): List<SampleCacheEntity>
}