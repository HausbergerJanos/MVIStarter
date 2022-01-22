package com.hausberger.mvistarter.framework.presentation.sample.state

import com.hausberger.mvistarter.R
import com.hausberger.mvistarter.business.domain.state.StateEvent
import kotlin.random.Random

sealed class SampleStateEvent : StateEvent {

    object FetchSampleEvent : SampleStateEvent() {

        override fun errorInfoRes(): Int {
            return R.string.sample_error
        }

        override fun eventName(): String {
            return "FetchSampleEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return true
        }
    }
}