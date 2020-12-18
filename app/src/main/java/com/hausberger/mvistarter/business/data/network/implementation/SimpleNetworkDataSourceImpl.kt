package com.hausberger.mvistarter.business.data.network.implementation

import com.hausberger.mvistarter.business.data.network.abstraction.SimpleNetworkDataSource
import com.hausberger.mvistarter.business.domain.model.Sample
import com.hausberger.mvistarter.framework.datasource.network.abstarction.SampleNetworkService

class SimpleNetworkDataSourceImpl
constructor(
    private val sampleNetworkService: SampleNetworkService
) : SimpleNetworkDataSource {

    override suspend fun getSamples(): List<Sample> {
        return sampleNetworkService.getSamples()
    }
}