package com.hausberger.mvistarter.business.interactors

import com.hausberger.mvistarter.R
import com.hausberger.mvistarter.business.data.cache.CacheResponseHandler
import com.hausberger.mvistarter.business.data.cache.abstraction.SampleCacheDataSource
import com.hausberger.mvistarter.business.data.network.ApiResponseHandler
import com.hausberger.mvistarter.business.data.util.safeApiCall
import com.hausberger.mvistarter.business.data.util.safeCacheCall
import com.hausberger.mvistarter.business.domain.model.Sample
import com.hausberger.mvistarter.business.domain.state.*
import com.hausberger.mvistarter.framework.datasource.network.abstarction.SampleNetworkService
import com.hausberger.mvistarter.framework.presentation.sample.state.SampleViewState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class SampleInteractor
@Inject
constructor(
    private val sampleCacheDataSource: SampleCacheDataSource,
    private val sampleNetworkService: SampleNetworkService
) {

    /**
     * Fetch samples and update cache. First get data from cache. After that, get samples from network.
     * If network fetching was successful update local database. So the steps:
     *
     * 1) Fetch data from [SampleCacheDataSource] and emit it immediately
     * 2) Fetch data form [SampleNetworkService]
     * 3) Update cache
     * 4) Finally fetch data from [SampleCacheDataSource] with updated values and emit it
     *
     * @param stateEvent:   Actual [StateEvent] which behaves as a meta data about event. Contains
     *                      information (such as event name, error info etc.)
     *
     * @return              a [Flow] which contains the data or error information
     *                      wrapped into a DataState
     */
    fun fetchSamples(
        stateEvent: StateEvent
    ): Flow<DataState<SampleViewState>?> = flow {

        // Fetch samples from cache and wrap it into DataState<SampleViewState>
        var cacheResponse = fetchSamplesFromCache(stateEvent)
        // emmit immediately cached DataState
        emit(cacheResponse)

        // Fetch samples from network and wrap it into DataState<SampleViewState>
        val apiResponse = fetchSamplesFromNetwork(stateEvent)
        apiResponse?.stateMessage?.response?.message?.getMessageRes()?.let { stateMessageRes ->
            if (stateMessageRes == R.string.network_data_fetched) {
                // Fetching from network was successful. Update cached data source!
                updateSampleCache(apiResponse)

                // Fetch samples from cache again. This will contains updated values too.
                // Wrap it into DataState<SampleViewState>
                cacheResponse = fetchSamplesFromCache(stateEvent)
                // emmit cached DataState
                emit(cacheResponse)
            } else {
                emit(apiResponse)
            }
        }
    }

    /**
     * Fetch samples from [SampleCacheDataSource]. If fetching was successful wrap results into
     * [DataState] with [R.string.cached_data_fetched] response message. Use [safeCacheCall] to safety handle
     * network calling.
     *
     * @param stateEvent:   actual [StateEvent] which behaves as a meta data about event. Contains
     *                      information (such as event name, error info etc.)
     *
     * @return              a [DataState] which contains the domain data or error information
     */
    private suspend fun fetchSamplesFromCache(
        stateEvent: StateEvent
    ): DataState<SampleViewState>? {
        val cacheResult = safeCacheCall(IO) {
            sampleCacheDataSource.getSamples()
        }

        return object : CacheResponseHandler<SampleViewState, List<Sample>>(
            response = cacheResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: List<Sample>): DataState<SampleViewState>? {
                return DataState.data(
                    response = Response(
                        message = SimpleMessage(
                            messageRes = R.string.cached_data_fetched
                        ),
                        uiComponentType = UIComponentType.None,
                        messageType = MessageType.Success
                    ),
                    data = SampleViewState(
                        samples = resultObj
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()
    }

    /**
     * Fetch samples from [SampleNetworkService]. If fetching was successful wrap results into
     * [DataState] with [R.string.network_data_fetched] response message. Use [safeApiCall] to safety handle
     * network calling.
     *
     * @param stateEvent:   actual [StateEvent] which behaves as a meta data about event. Contains
     *                      information (such as event name, error info etc.)
     *
     * @return              a [DataState] which contains the domain data or error information
     */
    private suspend fun fetchSamplesFromNetwork(
        stateEvent: StateEvent
    ): DataState<SampleViewState>? {
        val apiResult = safeApiCall(IO) {
            sampleNetworkService.getSamples()
        }

        return object : ApiResponseHandler<SampleViewState, List<Sample>>(
            response = apiResult,
            stateEvent = stateEvent
        ) {
            override suspend fun handleSuccess(resultObj: List<Sample>): DataState<SampleViewState>? {
                return DataState.data(
                    response = Response(
                        message = SimpleMessage(
                            messageRes = R.string.network_data_fetched
                        ),
                        uiComponentType = UIComponentType.None,
                        messageType = MessageType.Success
                    ),
                    data = SampleViewState(
                        samples = resultObj
                    ),
                    stateEvent = stateEvent
                )
            }
        }.getResult()
    }

    /**
     * Update [SampleCacheDataSource] with data from network.
     *
     * @param dataState     contains the domain data wrapped int a [DataState]. We need the "data"
     *                      attribute
     */
    private suspend fun updateSampleCache(dataState: DataState<SampleViewState>) {
        safeCacheCall(IO) {
            dataState.data?.samples?.forEach { sample ->
                sampleCacheDataSource.insert(sample)
            }
        }
    }
}