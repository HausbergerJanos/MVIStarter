package com.hausberger.mvistarter.framework.datasource.cache.abstraction

import com.hausberger.mvistarter.business.domain.model.Sample

interface SampleDaoService {

    suspend fun insert(sample: Sample): Long

    suspend fun getSamples(): List<Sample>
}