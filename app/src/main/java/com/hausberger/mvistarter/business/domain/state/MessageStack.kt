package com.hausberger.mvistarter.business.domain.state

import com.hausberger.mvistarter.util.printLogD
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MessageStack : ArrayList<StateMessage>() {

    private val _stateMessage: MutableStateFlow<StateMessage?> = MutableStateFlow(null)
    val stateMessage = _stateMessage.asStateFlow()

    fun isStackEmpty(): Boolean = size == 0

    override fun addAll(elements: Collection<StateMessage>): Boolean {
        elements.forEach { item ->
            add(item)
        }

        return true // always return true. We don't care about result bool.
    }

    override fun add(element: StateMessage): Boolean {
        if (contains(element)) {    // prevent duplicate errors added to stack
            return false
        }

        val transaction = super.add(element)
        if (size == 1) {
            setStateMessage(
                stateMessage = element
            )
        }

        return transaction
    }

    override fun removeAt(index: Int): StateMessage {
        try {
            val transaction = super.removeAt(index)
            if (size > 0) {
                setStateMessage(stateMessage = this[0])
            } else {
                printLogD("MessageStack", "stack is empty: ")
                setStateMessage(null)
            }
            return transaction
        } catch (e: IndexOutOfBoundsException) {
            setStateMessage(null)
            e.printStackTrace()
        }
        return StateMessage( // this does nothing
            Response(
                message = SimpleMessage(),
                uiComponentType = UIComponentType.None,
                messageType = MessageType.None
            )
        )
    }

    private fun setStateMessage(stateMessage: StateMessage?) {
        stateMessage?.let {
            _stateMessage.value = _stateMessage.value?.copy(
                response = it.response
            )
        }

        _stateMessage.value =  stateMessage
    }
}