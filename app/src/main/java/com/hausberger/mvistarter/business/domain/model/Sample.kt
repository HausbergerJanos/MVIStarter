package com.hausberger.mvistarter.business.domain.model

import java.io.Serializable

@kotlinx.serialization.Serializable
data class Sample(
    val title: String
): Serializable