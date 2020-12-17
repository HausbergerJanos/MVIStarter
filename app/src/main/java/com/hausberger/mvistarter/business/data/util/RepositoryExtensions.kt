package com.hausberger.mvistarter.business.data.util

import com.hausberger.mvistarter.business.data.cache.CacheResult
import com.hausberger.mvistarter.business.data.network.ApiResult
import com.hausberger.mvistarter.util.Constants.CacheConstants.Companion.CACHE_TIMEOUT
import com.hausberger.mvistarter.util.Constants.CacheErrors.Companion.CACHE_ERROR_TIMEOUT
import com.hausberger.mvistarter.util.Constants.CacheErrors.Companion.CACHE_ERROR_UNKNOWN
import com.hausberger.mvistarter.util.Constants.GenericErrors.Companion.ERROR_UNKNOWN
import com.hausberger.mvistarter.util.Constants.NetworkConstants.Companion.NETWORK_TIMEOUT
import com.hausberger.mvistarter.util.Constants.NetworkErrors.Companion.NETWORK_ERROR_TIMEOUT
import com.hausberger.mvistarter.util.Constants.NetworkErrors.Companion.NETWORK_ERROR_UNKNOWN
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
                    val code = 408 // timeout error code
                    ApiResult.GenericError(code, NETWORK_ERROR_TIMEOUT)
                }
                is IOException -> {
                    ApiResult.NetworkError
                }
                is HttpException -> {
                    val code = throwable.code()
                    val errorResponse = convertErrorBody(throwable)
                    ApiResult.GenericError(
                        code,
                        errorResponse
                    )
                }
                else -> {
                    ApiResult.GenericError(
                        null,
                        NETWORK_ERROR_UNKNOWN
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
                    CacheResult.GenericError(CACHE_ERROR_TIMEOUT)
                }
                else -> {
                    CacheResult.GenericError(CACHE_ERROR_UNKNOWN)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        ERROR_UNKNOWN
    }
}