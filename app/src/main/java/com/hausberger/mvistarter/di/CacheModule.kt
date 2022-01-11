package com.hausberger.mvistarter.di

import android.content.Context
import androidx.room.Room
import com.hausberger.mvistarter.business.data.cache.abstraction.SampleCacheDataSource
import com.hausberger.mvistarter.business.data.cache.implementation.SampleCacheDataSourceImpl
import com.hausberger.mvistarter.framework.datasource.cache.abstraction.SampleDaoService
import com.hausberger.mvistarter.framework.datasource.cache.database.AppDatabase
import com.hausberger.mvistarter.framework.datasource.cache.database.SampleDao
import com.hausberger.mvistarter.framework.datasource.cache.implementation.SampleDaoServiceImpl
import com.hausberger.mvistarter.framework.datasource.cache.mapper.SampleCacheMapper
import com.hausberger.mvistarter.util.Constants.AppConstants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CacheModule {

    @Singleton
    @Provides
    fun provideAppDb(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideSampleDao(appDatabase: AppDatabase): SampleDao {
        return appDatabase.sampleDao()
    }

    @Singleton
    @Provides
    fun provideSampleDaoService(
        sampleDao: SampleDao,
        sampleCacheMapper: SampleCacheMapper
    ): SampleDaoService {
        return SampleDaoServiceImpl(
            sampleDao = sampleDao,
            sampleCacheMapper = sampleCacheMapper
        )
    }

    @Singleton
    @Provides
    fun provideSampleCacheDataSource(
        sampleDaoService: SampleDaoService
    ): SampleCacheDataSource {
        return SampleCacheDataSourceImpl(sampleDaoService)
    }
}