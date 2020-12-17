package com.hausberger.mvistarter.business.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sample(
    val title: String
): Parcelable