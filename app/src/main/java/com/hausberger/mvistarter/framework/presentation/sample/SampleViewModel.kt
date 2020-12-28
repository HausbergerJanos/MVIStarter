package com.hausberger.mvistarter.framework.presentation.sample

import android.os.Parcelable
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hausberger.mvistarter.business.domain.model.Sample
import com.hausberger.mvistarter.business.domain.state.DataState
import com.hausberger.mvistarter.business.domain.state.StateEvent
import com.hausberger.mvistarter.business.interactors.SampleInteractor
import com.hausberger.mvistarter.framework.presentation.common.BaseViewModel
import com.hausberger.mvistarter.framework.presentation.sample.state.SampleStateEvent.*
import com.hausberger.mvistarter.framework.presentation.sample.state.SampleViewState
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.*

class SampleViewModel
@ViewModelInject
constructor(
    private val sampleInteractor: SampleInteractor,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel<SampleViewState>() {

    override fun handleNewData(data: SampleViewState) {

        data.let { viewState ->
            viewState.samples?.let { sampleList ->
                setSamplesListData(sampleList)
            }
        }
    }

    override fun setStateEvent(stateEvent: StateEvent) {
        val job: Flow<DataState<SampleViewState>?> = when (stateEvent) {
            is FetchSampleEvent -> {
                sampleInteractor.fetchSamples(stateEvent)
            }

            else -> {
                emitInvalidStateEvent(stateEvent)
            }
        }

        launchJob(stateEvent, job)
    }

    override fun initNewViewState(): SampleViewState {
        return SampleViewState()
    }

    private fun setSamplesListData(samples: List<Sample>) {
        val update = getCurrentViewStateOrNew()
        update.samples = samples
        setViewState(update)
    }

    fun getLayoutManagerState(): Parcelable? {
        return getCurrentViewStateOrNew().layoutManager
    }

    fun setLayoutManagerState(layoutManagerState: Parcelable) {
        val update = getCurrentViewStateOrNew()
        update.layoutManager = layoutManagerState
        setViewState(update)
    }
}