package com.hausberger.mvistarter.framework.datasource.network.implementation

import com.hausberger.mvistarter.business.domain.model.Sample
import com.hausberger.mvistarter.framework.datasource.network.abstarction.SampleNetworkService
import com.hausberger.mvistarter.framework.datasource.network.api.SampleApiService
import com.hausberger.mvistarter.framework.datasource.network.mapper.SampleNetworkMapper
import com.hausberger.mvistarter.util.Constants.NetworkConstants.Companion.API_KEY
import com.hausberger.mvistarter.util.Constants.NetworkConstants.Companion.API_LANGUAGE_CODE
import javax.inject.Inject

class SampleNetworkServiceImpl
@Inject
constructor(
    private val sampleApiService: SampleApiService,
    private val sampleNetworkMapper: SampleNetworkMapper
) : SampleNetworkService {

    override suspend fun getSamples(): List<Sample> {
        return sampleNetworkMapper.entityListToNoteList(
            entityListItems = sampleApiService.getSamples(API_LANGUAGE_CODE, API_KEY).articles
        )
    }
}