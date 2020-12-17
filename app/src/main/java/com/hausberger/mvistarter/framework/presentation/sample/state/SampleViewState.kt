package com.hausberger.mvistarter.framework.presentation.sample.state

import android.os.Parcelable
import com.hausberger.mvistarter.business.domain.model.Sample
import com.hausberger.mvistarter.business.domain.state.ViewState
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SampleViewState(
    var samples: List<Sample>? = null
) : ViewState, Parcelable