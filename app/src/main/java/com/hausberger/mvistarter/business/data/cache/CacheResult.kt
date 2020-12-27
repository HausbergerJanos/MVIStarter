package com.hausberger.mvistarter.business.data.cache

sealed class CacheResult<out T> {

    data class Success<out T>(val value: T) : CacheResult<T>()

    data class GenericError(
        val errorMessageRes: Int? = null
    ) : CacheResult<Nothing>()
}