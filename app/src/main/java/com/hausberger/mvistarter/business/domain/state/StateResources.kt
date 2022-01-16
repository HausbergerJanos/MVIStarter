package com.hausberger.mvistarter.business.domain.state

import android.view.View

data class StateMessage(val response: Response)

data class Response(
    val message: SimpleMessage?,
    val uiComponentType: UIComponentType,
    val messageType: MessageType
)

data class SimpleMessage(
    val messageCode: Int? = null,
    val messageRes: Int? = null,
    val description: String? = null
)

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