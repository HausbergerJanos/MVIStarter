package com.hausberger.mvistarter.framework.datasource.network.abstarction

import com.hausberger.mvistarter.business.domain.model.Sample

interface SampleNetworkService {

    suspend fun getSamples(): List<Sample>
}