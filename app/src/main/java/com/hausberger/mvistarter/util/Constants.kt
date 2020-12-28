package com.hausberger.mvistarter.util

interface Constants {

    interface AppConstants {
        companion object {
            const val TAG = "AppDebug" // Tag for logs
            const val DEBUG = true // enable logging
            const val DATABASE_NAME = "app_database"
        }
    }

    interface BundleKeys {
        companion object {
            const val BOTTOM_NAV_BACK_STACK_KEY = "bottom_nav_back_stack_key"
            const val SAMPLE_BUNDLE_KEY = "sample_bundle_key"
        }
    }

    interface CacheConstants {
        companion object {
            const val CACHE_TIMEOUT = 6000L
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
}