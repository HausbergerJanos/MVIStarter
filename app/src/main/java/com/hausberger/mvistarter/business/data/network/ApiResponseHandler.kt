package com.hausberger.mvistarter.business.data.network

import com.hausberger.mvistarter.R
import com.hausberger.mvistarter.business.domain.state.*

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
                            messageRes = stateEvent?.errorInfoRes(),
                            description = response.errorMessage,
                            descriptionRes = response.errorMessageRes
                        ),
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.NetworkError -> {
                DataState.error(
                    response = Response(
                        message = SimpleMessage(
                            messageRes = stateEvent?.errorInfoRes(),
                            descriptionRes = R.string.network_no_connection
                        ),
                        uiComponentType = UIComponentType.Dialog,
                        messageType = MessageType.Error
                    ),
                    stateEvent = stateEvent
                )
            }

            is ApiResult.Success -> {
                if (response.value == null) {
                    DataState.error(
                        response = Response(
                            message = SimpleMessage(
                                messageRes = stateEvent?.errorInfoRes(),
                                descriptionRes = R.string.network_data_null
                            ),
                            uiComponentType = UIComponentType.Dialog,
                            messageType = MessageType.Error
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