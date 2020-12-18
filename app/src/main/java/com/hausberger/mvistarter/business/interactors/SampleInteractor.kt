package com.hausberger.mvistarter.business.interactors

import com.hausberger.mvistarter.business.data.cache.CacheResponseHandler
import com.hausberger.mvistarter.business.data.cache.abstraction.SampleCacheDataSource
import com.hausberger.mvistarter.business.data.network.ApiResponseHandler
import com.hausberger.mvistarter.business.data.util.safeApiCall
import com.hausberger.mvistarter.business.data.util.safeCacheCall
import com.hausberger.mvistarter.business.domain.model.Sample
import com.hausberger.mvistarter.business.domain.state.*
import com.hausberger.mvistarter.framework.datasource.network.abstarction.SampleNetworkService
import com.hausberger.mvistarter.framework.presentation.sample.state.SampleViewState
import com.hausberger.mvistarter.util.Constants.CacheSuccess.Companion.CACHE_DATA_FETCHED
import com.hausberger.mvistarter.util.Constants.NetworkSuccess.Companion.NETWORK_DATA_FETCHED
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SampleInteractor
@Inject
constructor(
    private val sampleCacheDataSource: SampleCacheDataSource,
    private val sampleNetworkService: SampleNetworkService
) {

    fun fetchSamples(
        stateEvent: StateEvent
    ): Flow<DataState<SampleViewState>?> = flow {

        var cacheResult = safeCacheCall(IO) {
            sampleCacheDataSource.getSamples()
        }

        var response = object : CacheResponseHandler<SampleViewState, List<Sample>>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: List<Sample>): DataState<SampleViewState>? {
                return DataState.data(
                    response = Response(
                        message = CACHE_DATA_FETCHED,
                        uiComponentType = UIComponentType.Toast,
                        messageType = MessageType.Success
                    ),
                    data = SampleViewState(
                        samples = resultObj
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)

        val apiResult = safeApiCall(IO) {
            sampleNetworkService.getSamples()
        }

        val apiResponse = object : ApiResponseHandler<SampleViewState, List<Sample>>(
            response = apiResult,
                stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: List<Sample>): DataState<SampleViewState>? {
                return DataState.data(
                        response = Response(
                                message = NETWORK_DATA_FETCHED,
                                uiComponentType = UIComponentType.Toast,
                                messageType = MessageType.Success
                        ),
                        data = SampleViewState(
                                samples = resultObj
                        ),
                        stateEvent = stateEvent
                )
            }
        }.getResult()

        apiResponse?.stateMessage?.response?.message?.let { message ->
            if (message == NETWORK_DATA_FETCHED) {
                safeCacheCall(IO) {
                    apiResponse.data?.samples?.forEach { sample ->
                        sampleCacheDataSource.insert(sample)
                    }
                }
            }
        }

        cacheResult = safeCacheCall(IO) {
            sampleCacheDataSource.getSamples()
        }

        response = object : CacheResponseHandler<SampleViewState, List<Sample>>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: List<Sample>): DataState<SampleViewState>? {
                return DataState.data(
                    response = Response(
                        message = CACHE_DATA_FETCHED,
                        uiComponentType = UIComponentType.Toast,
                        messageType = MessageType.Success
                    ),
                    data = SampleViewState(
                        samples = resultObj
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()

        emit(response)
    }
}