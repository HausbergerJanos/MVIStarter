package com.hausberger.mvistarter.business.data.util

import com.google.gson.Gson
import com.hausberger.mvistarter.R
import com.hausberger.mvistarter.business.data.cache.CacheResult
import com.hausberger.mvistarter.business.data.network.ApiResult
import com.hausberger.mvistarter.framework.datasource.network.model.SampleApiError
import com.hausberger.mvistarter.util.Constants
import com.hausberger.mvistarter.util.Constants.CacheConstants.Companion.CACHE_TIMEOUT
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.CACHE_TIMEOUT_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.CACHE_UNKNOWN_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.NETWORK_TIMEOUT_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.NETWORK_UNKNOWN_ERROR
import com.hausberger.mvistarter.util.Constants.NetworkConstants.Companion.NETWORK_TIMEOUT
import com.hausberger.mvistarter.util.cLog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import retrofit2.HttpException
import java.io.IOException

/**
 * Reference: https://medium.com/@douglas.iacovelli/how-to-handle-errors-with-retrofit-and-coroutines-33e7492a912
 */
suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T?
): ApiResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(NETWORK_TIMEOUT) {
                ApiResult.Success(apiCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            cLog(throwable.message)
            when (throwable) {
                is TimeoutCancellationException -> {
                    val code =
                        Constants.ErrorType.NETWORK_TIMEOUT_ERROR // timeout error code
                    ApiResult.GenericError(
                        code = code
                    )
                }
                is IOException -> {
                    ApiResult.NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    ApiResult.GenericError(
                        code = code,
                        errorMessage = errorResponse
                    )
                }
                else -> {
                    ApiResult.GenericError(
                        code = NETWORK_UNKNOWN_ERROR
                    )
                }
            }
        }
    }
}

suspend fun <T> safeCacheCall(
    dispatcher: CoroutineDispatcher,
    cacheCall: suspend () -> T?
) : CacheResult<T?> {
    return withContext(dispatcher) {
        try {
            // throws TimeoutCancellationException
            withTimeout(CACHE_TIMEOUT) {
                CacheResult.Success(cacheCall.invoke())
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            cLog(throwable.message)
            when (throwable) {
                is TimeoutCancellationException -> {
                    CacheResult.GenericError(
                        code = CACHE_TIMEOUT_ERROR
                    )
                }
                else -> {
                    CacheResult.GenericError(
                        code = CACHE_UNKNOWN_ERROR
                    )
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    var body: String? = ""
    try {
        body = throwable.response()?.errorBody()?.string()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return try {
        val gson = Gson()
        val adapter = gson.getAdapter(SampleApiError::class.java)
        val apiError = adapter.fromJson(body)
        apiError.message
    } catch (e: Exception) {
        e.printStackTrace()
        body
    }

//    return try {
//        throwable.response()?.errorBody()?.string()
//    } catch (exception: Exception) {
//        ""
//    }
}