package com.hausberger.mvistarter.di

import com.hausberger.mvistarter.framework.presentation.common.error.ErrorManager
import com.hausberger.mvistarter.framework.presentation.common.error.ErrorMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideErrorManager(): ErrorManager {
        return ErrorManager(
            errorMapper = ErrorMapper
        )
    }
}