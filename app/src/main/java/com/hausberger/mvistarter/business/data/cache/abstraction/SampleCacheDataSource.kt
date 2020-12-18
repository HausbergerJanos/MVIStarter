package com.hausberger.mvistarter.business.data.cache.abstraction

import com.hausberger.mvistarter.business.domain.model.Sample

interface SampleCacheDataSource {

    suspend fun insert(sample: Sample): Long

    suspend fun getSamples(): List<Sample>
}