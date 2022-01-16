package com.hausberger.mvistarter.framework.presentation.common.error

import com.hausberger.mvistarter.R
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.CACHE_TIMEOUT_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.CACHE_UNKNOWN_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.NETWORK_DATA_NULL_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.NETWORK_NO_CONNECTION_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.NETWORK_TIMEOUT_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.NETWORK_UNKNOWN_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.UNDEFINED_ERROR_CODE

object ErrorMapper {
    val errorsMap: Map<Int, Int>
        get() = mapOf(
            Pair(CACHE_TIMEOUT_ERROR, R.string.cache_timeout),
            Pair(CACHE_UNKNOWN_ERROR, R.string.cache_unknown_error),
            Pair(NETWORK_TIMEOUT_ERROR, R.string.network_timeout),
            Pair(NETWORK_UNKNOWN_ERROR, R.string.network_unknown_error),
            Pair(NETWORK_NO_CONNECTION_ERROR, R.string.network_no_connection),
            Pair(NETWORK_DATA_NULL_ERROR, R.string.network_data_null)
        ).withDefault { UNDEFINED_ERROR_CODE }
}