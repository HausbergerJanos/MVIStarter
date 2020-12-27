package com.hausberger.mvistarter.business.data.util

import com.hausberger.mvistarter.R
import com.hausberger.mvistarter.business.data.cache.CacheResult
import com.hausberger.mvistarter.business.data.network.ApiResult
import com.hausberger.mvistarter.util.Constants.CacheConstants.Companion.CACHE_TIMEOUT
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
                    val code = 408 // timeout error code
                    ApiResult.GenericError(
                        code = code,
                        errorMessageRes = R.string.network_timeout
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
                        errorMessage = errorResponse,
                        errorMessageRes = if (errorResponse.isNullOrEmpty()) R.string.network_unknown_error else null
                    )
                }
                else -> {
                    ApiResult.GenericError(
                        code = null,
                        errorMessageRes = R.string.network_unknown_error
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
                    CacheResult.GenericError(R.string.cache_timeout)
                }
                else -> {
                    CacheResult.GenericError(R.string.cache_unknown_error)
                }
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        throwable.response()?.errorBody()?.string()
    } catch (exception: Exception) {
        ""
    }
}