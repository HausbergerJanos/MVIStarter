package com.hausberger.mvistarter.framework.presentation.common.error

import android.content.Context
import androidx.annotation.IntegerRes
import com.hausberger.mvistarter.business.domain.state.SimpleMessage
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.UNDEFINED_ERROR_CODE
import javax.inject.Inject

class ErrorManager
@Inject
constructor(
    private val errorMapper: ErrorMapper
) {

    fun getError(context: Context, message: SimpleMessage): String {
        var errorMessage = ""

        // Get error. Usually it comes from StateEvent
        message.messageRes?.let {
            errorMessage = context.getString(it)
        }

        // Get cause of error
        message.messageCode?.let {
            val errorRes = getErrorRes(it)
            if (errorRes != UNDEFINED_ERROR_CODE) {
                errorMessage = "$errorMessage\n\n${context.getString(errorRes)}"
            }
        }

        // Append description if it is available
        message.description?.let { description ->
            if (description.isNotEmpty()) {
                errorMessage = "$errorMessage\n\n$description"
            }
        }

        return errorMessage
    }

    private fun getErrorRes(errorCode: Int): Int {
        return errorMapper.errorsMap.getValue(errorCode)
    }
}