package com.hausberger.mvistarter.business.data.network.abstraction

import com.hausberger.mvistarter.business.domain.model.Sample

interface SimpleNetworkDataSource {

    suspend fun getSamples(): List<Sample>
}