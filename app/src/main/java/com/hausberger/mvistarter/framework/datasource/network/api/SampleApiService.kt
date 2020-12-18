package com.hausberger.mvistarter.framework.datasource.network.api

import com.hausberger.mvistarter.framework.datasource.network.model.SampleNetworkEntity
import retrofit2.http.GET
import retrofit2.http.Query

interface SampleApiService {

    @GET("top-headlines")
    suspend fun getSamples(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String
    ): SampleNetworkEntity
}