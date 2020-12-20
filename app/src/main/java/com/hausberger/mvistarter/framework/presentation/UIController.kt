package com.hausberger.mvistarter.framework.presentation

import com.hausberger.mvistarter.business.domain.state.Response
import com.hausberger.mvistarter.business.domain.state.StateMessageCallback

interface UIController {

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )
}