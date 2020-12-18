package com.hausberger.mvistarter.business.data.cache.implementation

import com.hausberger.mvistarter.business.data.cache.abstraction.SampleCacheDataSource
import com.hausberger.mvistarter.business.domain.model.Sample
import com.hausberger.mvistarter.framework.datasource.cache.abstraction.SampleDaoService
import javax.inject.Inject

class SampleCacheDataSourceImpl
@Inject
constructor(
    private val sampleDaoService: SampleDaoService
) : SampleCacheDataSource {

    override suspend fun insert(sample: Sample): Long {
        return sampleDaoService.insert(sample)
    }

    override suspend fun getSamples(): List<Sample> {
        return sampleDaoService.getSamples()
    }
}