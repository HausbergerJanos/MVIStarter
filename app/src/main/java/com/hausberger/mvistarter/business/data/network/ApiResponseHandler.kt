package com.hausberger.mvistarter.business.data.network

import com.hausberger.mvistarter.business.domain.state.*
import com.hausberger.mvistarter.business.domain.state.MessageType.*
import com.hausberger.mvistarter.business.domain.state.UIComponentType.*
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.NETWORK_DATA_NULL_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.NETWORK_NO_CONNECTION_ERROR

abstract class ApiResponseHandler<ViewState, Data>(
    private val response: ApiResult<Data?>,
    private val stateEvent: StateEvent?
) {

    suspend fun getResult(): DataState<ViewState>? {
        return when (response) {
            is ApiResult.GenericError -> {
                DataState.error(
                    response = Response(
                        message = SimpleMessage(
                            messageCode = response.code,
                            messageRes = stateEvent?.errorInfoRes(),
                            description = response.errorMessage
                        ),
                        uiComponentType = Dialog,
                        messageType = Error
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.NetworkError -> {
                DataState.error(
                    response = Response(
                        message = SimpleMessage(
                            messageCode = NETWORK_NO_CONNECTION_ERROR,
                            messageRes = stateEvent?.errorInfoRes()
                        ),
                        uiComponentType = Dialog,
                        messageType = Error
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.Success -> {
                if (response.value == null) {
                    DataState.error(
                        response = Response(
                            message = SimpleMessage(
                                messageCode = NETWORK_DATA_NULL_ERROR,
                                messageRes = stateEvent?.errorInfoRes()
                            ),
                            uiComponentType = Dialog,
                            messageType = Error
                        ),
                        stateEvent = stateEvent
                    )
                } else {
                    handleSuccess(resultObj = response.value)
                }
            }
        }
    }

    abstract suspend fun handleSuccess(resultObj: Data): DataState<ViewState>?
}