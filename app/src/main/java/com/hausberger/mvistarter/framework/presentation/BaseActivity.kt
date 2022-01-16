package com.hausberger.mvistarter.framework.presentation

import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.hausberger.mvistarter.R
import com.hausberger.mvistarter.business.domain.state.*
import com.hausberger.mvistarter.framework.presentation.common.UIController
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.CACHE_TIMEOUT_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.CACHE_UNKNOWN_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.NETWORK_DATA_NULL_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.NETWORK_NO_CONNECTION_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.NETWORK_TIMEOUT_ERROR
import com.hausberger.mvistarter.util.Constants.ErrorType.Companion.NETWORK_UNKNOWN_ERROR

open class BaseActivity : AppCompatActivity(),
    UIController {

    private var dialogInView: MaterialDialog? = null

    override fun onPause() {
        super.onPause()
        if (dialogInView != null) {
            (dialogInView as MaterialDialog).dismiss()
            dialogInView = null
        }
    }

    override fun onResponseReceived(response: Response, stateMessageCallback: StateMessageCallback) {
        when (response.uiComponentType) {
//            is UIComponentType.SnackBar -> {
//                val onDismissCallback: TodoCallback? = response.uiComponentType.dismissCallback
//                val undoCallback: UndoCallback? = response.uiComponentType.undoCallback
//                response.message?.let { msg ->
//                    displaySnackbar(
//                        message = msg.getMessageX(this),
//                        snackbarUndoCallback = undoCallback,
//                        onDismissCallback = onDismissCallback,
//                        stateMessageCallback = stateMessageCallback
//                    )
//                }
//            }
//
//            is UIComponentType.AreYouSureDialog -> {
//
//                response.message?.let {
//                    areYouSureDialog(
//                        message = it.getMessageX(this),
//                        callback = response.uiComponentType.callback,
//                        stateMessageCallback = stateMessageCallback
//                    )
//                }
//            }
//
//            is UIComponentType.Toast -> {
//                response.message?.let {
//                    displayToast(
//                        message = it.getMessageX(this),
//                        stateMessageCallback = stateMessageCallback
//                    )
//                }
//            }
//
            is UIComponentType.Dialog -> {
                displayDialog(
                    response = response,
                    stateMessageCallback = stateMessageCallback
                )
            }

            is UIComponentType.None -> {
                // This would be a good place to send to your Error Reporting
                // software of choice (ex: Firebase crash reporting)
                stateMessageCallback.removeMessageFromStack()
            }
        }
    }

//    private fun displaySnackbar(
//        message: String,
//        snackbarUndoCallback: UndoCallback?,
//        onDismissCallback: TodoCallback?,
//        stateMessageCallback: StateMessageCallback
//    ) {
//        val snackbar = Snackbar.make(
//            findViewById(R.id.mainRoot),
//            message,
//            Snackbar.LENGTH_LONG
//        )
//        snackbar.setAction(
//            getString(R.string.text_undo),
//            SnackbarUndoListener(snackbarUndoCallback)
//        )
//        snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
//            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
//                onDismissCallback?.execute()
//            }
//        })
//        snackbar.show()
//        stateMessageCallback.removeMessageFromStack()
//    }
//
    private fun displayDialog(
        response: Response,
        stateMessageCallback: StateMessageCallback
    ) {
        response.message?.let { message ->
            dialogInView = when (response.messageType) {
                is MessageType.Error -> {
                    displayErrorDialog(
                        message = createMessageWithReason(message),
                        stateMessageCallback = stateMessageCallback
                    )
                }

//                is MessageType.Success -> {
//                    displaySuccessDialog(
//                        message = message.getMessageX(this),
//                        stateMessageCallback = stateMessageCallback
//                    )
//                }
//
//                is MessageType.Info -> {
//                    displayInfoDialog(
//                        message = message.getMessageX(this),
//                        stateMessageCallback = stateMessageCallback
//                    )
//                }

                else -> {
                    // do nothing
                    stateMessageCallback.removeMessageFromStack()
                    null
                }
            }
        } ?: stateMessageCallback.removeMessageFromStack()
    }

    private fun displayErrorDialog(
        message: String?,
        stateMessageCallback: StateMessageCallback
    ): MaterialDialog {
        return MaterialDialog(this)
            .show {
                title(R.string.text_error)
                message(text = message)
                positiveButton(R.string.text_ok) {
                    stateMessageCallback.removeMessageFromStack()
                    dismiss()
                }
                onDismiss {
                    dialogInView = null
                }
                cancelable(false)
            }
    }
//
//    private fun displaySuccessDialog(
//        message: String?,
//        stateMessageCallback: StateMessageCallback
//    ): MaterialDialog {
//        return MaterialDialog(this)
//            .show {
//                title(R.string.text_success)
//                message(text = message)
//                positiveButton(R.string.text_ok) {
//                    stateMessageCallback.removeMessageFromStack()
//                    dismiss()
//                }
//                onDismiss {
//                    dialogInView = null
//                }
//                cancelable(false)
//            }
//    }
//
//    private fun displayInfoDialog(
//        message: String?,
//        stateMessageCallback: StateMessageCallback
//    ): MaterialDialog {
//        return MaterialDialog(this)
//            .show {
//                title(R.string.text_info)
//                message(text = message)
//                positiveButton(R.string.text_ok) {
//                    stateMessageCallback.removeMessageFromStack()
//                    dismiss()
//                }
//                onDismiss {
//                    dialogInView = null
//                }
//                cancelable(false)
//            }
//    }
//
//    private fun areYouSureDialog(
//        message: String,
//        callback: AreYouSureCallback,
//        stateMessageCallback: StateMessageCallback
//    ): MaterialDialog {
//        return MaterialDialog(this)
//            .show {
//                title(R.string.are_you_sure)
//                message(text = message)
//                negativeButton(R.string.text_cancel) {
//                    stateMessageCallback.removeMessageFromStack()
//                    callback.cancel()
//                    dismiss()
//                }
//                positiveButton(R.string.text_yes) {
//                    stateMessageCallback.removeMessageFromStack()
//                    callback.proceed()
//                    dismiss()
//                }
//                onDismiss {
//                    dialogInView = null
//                }
//                cancelable(false)
//            }
//    }
//
//    private fun createMessageWithReason(
//        message: SimpleMessage
//    ): String {
//        return getString(R.string.message_with_reason, message.getMessage(this), message.getDescription(this))
//    }

    // TODO - Remove
    private fun createMessageWithReason(
        message: SimpleMessage
    ): String {
        var errorMessage = ""
        message.messageRes?.let {
            errorMessage = getString(it)
        }

        message.messageCode?.let {
            when (it) {
                NETWORK_TIMEOUT_ERROR -> {
                    errorMessage = "$errorMessage\n\nTime out"
                }
                NETWORK_NO_CONNECTION_ERROR -> {
                    errorMessage = "$errorMessage\n\nNo Internet connection"
                }
                NETWORK_DATA_NULL_ERROR -> {
                    errorMessage = "$errorMessage\n\nNetwork data null"
                }
                NETWORK_UNKNOWN_ERROR -> {
                    errorMessage = "$errorMessage\n\nBla Bla Bla"
                }

                CACHE_TIMEOUT_ERROR -> {
                    errorMessage = "$errorMessage\n\nTime out Cache"
                }
                CACHE_UNKNOWN_ERROR -> {
                    errorMessage = "$errorMessage\n\nBla Bla Bla Cache"
                }
            }
        }

        message.description?.let { description ->
            if (description.isNotEmpty()) {
                errorMessage = "$errorMessage\n\n$description"
            }
        }

        return errorMessage
    }
}