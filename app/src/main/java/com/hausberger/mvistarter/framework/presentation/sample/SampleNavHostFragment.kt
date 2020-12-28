package com.hausberger.mvistarter.framework.presentation.sample

import android.content.Context
import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.navigation.fragment.NavHostFragment

class SampleNavHostFragment : NavHostFragment() {

    override fun onAttach(context: Context) {
        // override here if you want fragment constructor injection
        super.onAttach(context)
    }

    companion object {

        private const val KEY_GRAPH_ID = "android-support-nav:fragment:graphId"

        @JvmStatic
        fun create(
            @NavigationRes graphId: Int = 0
        ): SampleNavHostFragment {
            var bundle: Bundle? = null
            if (graphId != 0) {
                bundle = Bundle()
                bundle.putInt(KEY_GRAPH_ID, graphId)
            }
            val result =
                SampleNavHostFragment()
            if (bundle != null) {
                result.arguments = bundle
            }
            return result
        }
    }
}