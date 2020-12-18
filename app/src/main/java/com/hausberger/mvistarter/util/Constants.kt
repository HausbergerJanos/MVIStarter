package com.hausberger.mvistarter.util

interface Constants {

    interface Generics {
        companion object {
            const val TAG = "AppDebug" // Tag for logs
            const val DEBUG = true // enable logging
            const val DATABASE_NAME = "app_database"
        }
    }

    interface CacheConstants {
        companion object {
            const val CACHE_TIMEOUT = 6000L
        }
    }

    interface CacheErrors {
        companion object {
            const val CACHE_DATA_NULL = "Cache data is null"
            const val CACHE_ERROR_TIMEOUT = "Cache timeout"
            const val CACHE_ERROR_UNKNOWN = "Unknown cache error"
        }
    }

    interface CacheSuccess {
        companion object {
            const val CACHE_DATA_FETCHED = "Cache data fetched"
        }
    }

    interface NetworkConstants {
        companion object {
            const val API_BASE_URL = "https://newsapi.org/v2/"
            const val API_LANGUAGE_CODE = "hu"
            const val API_KEY = "df0796c1dec749f8a7296e59a2e4a1cd"
            const val NETWORK_TIMEOUT = 6000L
        }
    }

    interface NetworkErrors {
        companion object {
            const val NETWORK_ERROR = "Network error"
            const val NETWORK_DATA_NULL = "Network data is null"
            const val NETWORK_ERROR_TIMEOUT = "Network timeout"
            const val NETWORK_ERROR_UNKNOWN = "Unknown network error"
        }
    }

    interface NetworkSuccess {
        companion object {
            const val NETWORK_DATA_FETCHED = "Network data fetched"
        }
    }

    interface GenericErrors {
        companion object {
            const val ERROR_UNKNOWN = "Unknown error"
            const val INVALID_STATE_EVENT = "Invalid state event"
        }
    }
}