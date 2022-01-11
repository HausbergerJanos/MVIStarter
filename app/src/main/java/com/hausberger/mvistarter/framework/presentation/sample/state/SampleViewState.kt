package com.hausberger.mvistarter.framework.presentation.sample.state

import android.os.Parcelable
import com.hausberger.mvistarter.business.domain.model.Sample
import com.hausberger.mvistarter.business.domain.state.ViewState
import java.io.Serializable

@kotlinx.serialization.Serializable
data class SampleViewState(
    var samples: List<Sample>? = null,
    var layoutManager: Parcelable? = null
) : ViewState, Serializable