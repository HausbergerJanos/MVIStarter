package com.hausberger.mvistarter.framework.datasource.cache.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hausberger.mvistarter.framework.datasource.cache.model.SampleCacheEntity

@Database(
    entities = [SampleCacheEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun sampleDao(): SampleDao
}