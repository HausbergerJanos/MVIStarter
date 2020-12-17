package com.hausberger.mvistarter.framework.presentation.sample

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.hausberger.mvistarter.R
import com.hausberger.mvistarter.framework.presentation.sample.state.SampleStateEvent
import com.hausberger.mvistarter.framework.presentation.sample.state.SampleStateEvent.*
import com.hausberger.mvistarter.util.printLogD
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleFragment : Fragment(R.layout.fragment_sample) {

    private val viewModel: SampleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let { sampleViewState ->
                sampleViewState.samples?.let { sampleList ->
                    printLogD("TAG", "message")
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer { shouldDisplay ->
            printLogD("TAG", "message")
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let { safeStateMessage ->
                printLogD("TAG", "message")
            }
        })
    }

    override fun onResume() {
        super.onResume()

        viewModel.setStateEvent(FetchSampleEvent)
    }
}