package com.hausberger.mvistarter.framework.datasource.cache.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "samples")
data class SampleCacheEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "title")
    var title: String
)