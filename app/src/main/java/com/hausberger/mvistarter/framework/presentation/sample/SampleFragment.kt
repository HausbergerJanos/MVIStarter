package com.hausberger.mvistarter.framework.presentation.sample

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.hausberger.mvistarter.R
import com.hausberger.mvistarter.business.domain.model.Sample
import com.hausberger.mvistarter.business.domain.state.StateMessageCallback
import com.hausberger.mvistarter.databinding.FragmentSampleBinding
import com.hausberger.mvistarter.framework.presentation.common.UIController
import com.hausberger.mvistarter.framework.presentation.sample.SampleAdapter.*
import com.hausberger.mvistarter.framework.presentation.sample.state.SampleStateEvent.*
import com.hausberger.mvistarter.framework.presentation.sample.state.SampleViewState
import com.hausberger.mvistarter.util.Constants.BundleKeys.Companion.SAMPLE_BUNDLE_KEY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SampleFragment : Fragment(R.layout.fragment_sample), Interaction {
    private var currentBinding: FragmentSampleBinding? = null
    private val binding get() = currentBinding!!

    private val viewModel: SampleViewModel by viewModels()

    private var sampleAdapter: SampleAdapter? = null
    private lateinit var uiController: UIController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.setupChannel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentBinding = FragmentSampleBinding.bind(view)

        initRecyclerView()
        initSwipeRefresh()

        subscribeObservers()

        restoreInstanceState(savedInstanceState)
    }

    private fun restoreInstanceState(savedInstanceState: Bundle?){
        savedInstanceState?.let { inState ->
            (inState[SAMPLE_BUNDLE_KEY] as SampleViewState?)?.let { viewState ->
                viewModel.setViewState(viewState)
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            viewState?.let { sampleViewState ->
                sampleViewState.samples?.let { sampleList ->
                    sampleAdapter?.submitList(sampleList)
                }
            }
        })

        viewModel.shouldDisplayProgressBar.observe(viewLifecycleOwner, Observer { shouldDisplay ->

        })

        viewModel.stateMessage.observe(viewLifecycleOwner, Observer { stateMessage ->
            stateMessage?.let { safeStateMessage ->
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

    private fun initRecyclerView() {
        sampleAdapter = SampleAdapter(this)
        binding.sampleRecyclerView.apply {
            adapter = sampleAdapter
        }
    }

    private fun initSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    override fun onResume() {
        super.onResume()

        viewModel.setStateEvent(FetchSampleEvent)
    }

    override fun onPause() {
        super.onPause()

        if (viewModel.getMessageStackSize() > 0) {
            viewModel.clearStateMessage()
        }

        saveLayoutManagerState()
    }

    override fun restoreListPosition() {
        viewModel.getLayoutManagerState()?.let { lmState ->
            binding.sampleRecyclerView.layoutManager?.onRestoreInstanceState(lmState)
        }
    }

    private fun saveLayoutManagerState() {
        binding.sampleRecyclerView.layoutManager?.onSaveInstanceState()?.let { lmState ->
            viewModel.setLayoutManagerState(lmState)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val viewState = viewModel.viewState.value

        // Clear the list. Don't want to save a large list to bundle.
        viewState?.samples = emptyList()

        outState.putSerializable(
            SAMPLE_BUNDLE_KEY,
            viewState
        )
        super.onSaveInstanceState(outState)
    }

    override fun onItemSelected(position: Int, item: Sample) {
        Toast.makeText(requireContext(), item.title, Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_sampleFragment_to_detailsFragment)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is UIController) {
            uiController = context
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        sampleAdapter = null
        currentBinding = null
    }
}