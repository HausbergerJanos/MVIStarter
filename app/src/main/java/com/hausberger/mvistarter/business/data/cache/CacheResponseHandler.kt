package com.hausberger.mvistarter.business.data.cache

import com.hausberger.mvistarter.business.data.cache.CacheResult.*
import com.hausberger.mvistarter.business.domain.state.*

abstract class CacheResponseHandler<ViewState, Data>(
    private val response: CacheResult<Data?>,
    private val stateEvent: StateEvent?
) {

    suspend fun getResult(): DataState<ViewState>? {
        return when (response) {
            is GenericError -> {
                DataState.error(
                    response = Response(
                        message = SimpleMessage(
                            messageCode = response.code,
                            messageRes = stateEvent?.errorInfoRes(),
                        ),
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }

            is Success -> {
                if (response.value == null) {
                    DataState.error(
                        response = Response(
                            message = SimpleMessage(
                                messageRes = stateEvent?.errorInfoRes()
                            ),
                            uiComponentType = UIComponentType.Dialog,
                            messageType = MessageType.Error
                        ),
                        stateEvent = stateEvent
                    )
                } else {
                    handleSuccess(
                        resultObj = response.value
                    )
                }
            }
        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>?
}