package com.hausberger.mvistarter.framework.presentation.sample

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.hausberger.mvistarter.R
import com.hausberger.mvistarter.business.domain.state.StateMessageCallback
import com.hausberger.mvistarter.framework.presentation.UIController
import com.hausberger.mvistarter.framework.presentation.sample.state.SampleStateEvent
import com.hausberger.mvistarter.framework.presentation.sample.state.SampleStateEvent.*
import com.hausberger.mvistarter.util.printLogD
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi

@AndroidEntryPoint
class SampleFragment : Fragment(R.layout.fragment_sample) {

    private val viewModel: SampleViewModel by viewModels()

    private lateinit var uiController: UIController

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
                    printLogD("TAG-->", "on view state")
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer { shouldDisplay ->
            printLogD("TAG-->", "on progress bar")
        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let { safeStateMessage ->
                printLogD("TAG-->", "on state message $stateMessage")
                uiController.onResponseReceived(
                    response = safeStateMessage.response,
                    stateMessageCallback = object : StateMessageCallback {
                        override fun removeMessageFromStack() {
                            viewModel.clearStateMessage()
                        }
                    }
                )
            }
        })
    }

    override fun onResume() {
        super.onResume()

        viewModel.setStateEvent(FetchSampleEvent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is UIController) {
            uiController = context
        }
    }
}