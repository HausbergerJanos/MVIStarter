package com.hausberger.mvistarter.util

interface Constants {

    interface Generics {
        companion object {
            const val TAG = "AppDebug" // Tag for logs
            const val DEBUG = true // enable logging
        }
    }

    interface CacheErrors {
        companion object {
            const val CACHE_DATA_NULL = "Cache data is null"
            const val CACHE_ERROR_TIMEOUT = "Cache timeout"
            const val CACHE_ERROR_UNKNOWN = "Unknown cache error"
        }
    }

    interface NetworkConstants {
        companion object {
            const val NETWORK_TIMEOUT = 6000L
        }
    }

    interface CacheConstants {
        companion object {
            const val CACHE_TIMEOUT = 6000L
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

    interface GenericErrors {
        companion object {
            const val ERROR_UNKNOWN = "Unknown error"
            const val INVALID_STATE_EVENT = "Invalid state event"
        }
    }
}