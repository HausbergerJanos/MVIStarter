package com.hausberger.mvistarter.framework.presentation

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.hausberger.mvistarter.framework.presentation.common.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

abstract class BaseFragment(
    @LayoutRes val layoutRes: Int
) : Fragment(layoutRes) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupChannel()
    }

    override fun onPause() {
        super.onPause()
        clearMessageStack()
    }

    abstract fun setupChannel()

    abstract fun clearMessageStack()
}