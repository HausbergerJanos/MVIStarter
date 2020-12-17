package com.hausberger.mvistarter.framework.presentation.sample.state

import com.hausberger.mvistarter.business.domain.state.StateEvent

sealed class SampleStateEvent : StateEvent {

    object FetchSampleEvent : SampleStateEvent() {
        override fun errorInfo(): String {
            return "Error fetching samples."
        }

        override fun eventName(): String {
            return "FetchSampleEvent"
        }

        override fun shouldDisplayProgressBar(): Boolean {
            return true
        }
    }
}