package com.hausberger.mvistarter.framework.presentation.sample

import com.hausberger.mvistarter.business.domain.model.Sample
import com.hausberger.mvistarter.business.domain.state.DataFlowManager
import com.hausberger.mvistarter.business.domain.state.DataState
import com.hausberger.mvistarter.business.domain.state.StateEvent
import com.hausberger.mvistarter.business.interactors.SampleInteractor
import com.hausberger.mvistarter.framework.presentation.common.BaseViewModel
import com.hausberger.mvistarter.framework.presentation.sample.state.SampleStateEvent.*
import com.hausberger.mvistarter.framework.presentation.sample.state.SampleViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class SampleViewModel
@Inject
constructor(
    private val sampleInteractor: SampleInteractor
) : BaseViewModel<SampleViewState>() {

    override fun handleNewData(data: SampleViewState) {
        data.let { viewState ->
            viewState.samples?.let { sampleList ->
                setSamplesListData(sampleList)
            }
        }
    }

    /**
     * According to [StateEvent] creates a [DataState] flow and it will be launched
     * from the [DataFlowManager]
     */
    override fun setStateEvent(stateEvent: StateEvent) {
        val dataStateFlow: Flow<DataState<SampleViewState>?> = when (stateEvent) {
            is FetchSampleEvent -> {
                sampleInteractor.fetchSamples(stateEvent)
            }

            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }

        collectFlow(stateEvent, dataStateFlow)
    }

    override fun initNewViewState(): SampleViewState {
        return SampleViewState()
    }

    private fun setSamplesListData(samples: List<Sample>) {
        val update = getCurrentViewStateOrNew().copy(
            samples = samples
        )
        setViewState(update)
    }

    fun refresh() {
        setStateEvent(FetchSampleEvent)
    }
}