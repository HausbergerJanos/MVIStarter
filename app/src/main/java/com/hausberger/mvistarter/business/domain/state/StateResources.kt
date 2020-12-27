package com.hausberger.mvistarter.business.domain.state

import android.content.Context
import android.view.View
import androidx.annotation.StringRes
import java.lang.StringBuilder
import kotlin.collections.ArrayList

data class StateMessage(val response: Response)

data class Response(
    val message: SimpleMessage?,
    val uiComponentType: UIComponentType,
    val messageType: MessageType
)

data class SimpleMessage(
    private val message: String? = null,
    private val description: String? = null,
    private val messageRes: Int? = null,
    private val descriptionRes: Int? = null
) {

    fun getMessageRes(): Int? = messageRes

    fun getMessageX(
        context: Context?
    ): String {
        val message =  message
            ?: if (messageRes != null && context != null) {
                context.getString(messageRes)
            } else {
                ""
            }

        val description =  description
            ?: if (descriptionRes != null && context != null) {
                context.getString(descriptionRes)
            } else {
                ""
            }

        return message + description
    }

    fun getMessage(
        context: Context?
    ): String {
        return message
            ?: if (messageRes != null && context != null) {
                context.getString(messageRes)
            } else {
                ""
            }
    }

    fun getDescription(
        context: Context?
    ): String {
        return description
            ?: if (descriptionRes != null && context != null) {
                context.getString(descriptionRes)
            } else {
                ""
            }
    }
}

sealed class UIComponentType {

    object Toast : UIComponentType()

    object Dialog : UIComponentType()

    class AreYouSureDialog(
        val callback: AreYouSureCallback
    ) : UIComponentType()

    class SnackBar(
        val undoCallback: UndoCallback? = null,
        val dismissCallback: TodoCallback? = null
    ) : UIComponentType()

    object None : UIComponentType()
}

sealed class MessageType {

    object Success : MessageType()

    object Error : MessageType()

    object Info : MessageType()

    object None : MessageType()
}

interface AreYouSureCallback {

    fun proceed()

    fun cancel()
}

/**
 * Simple callback to undo something after a function is called
 */
interface UndoCallback {
    fun undo()
}

/**
 * Simple callback to execute something after a function is called
 */
interface TodoCallback {
    fun execute()
}

interface StateMessageCallback {
    fun removeMessageFromStack()
}

class SnackbarUndoListener
constructor(
    private val snackbarUndoCallback: UndoCallback?
) : View.OnClickListener {

    override fun onClick(v: View?) {
        snackbarUndoCallback?.undo()
    }
}