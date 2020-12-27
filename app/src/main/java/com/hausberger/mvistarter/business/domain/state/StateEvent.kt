package com.hausberger.mvistarter.business.domain.state

interface StateEvent {

    fun errorInfoRes(): Int

    fun eventName(): String

    fun shouldDisplayProgressBar(): Boolean
}