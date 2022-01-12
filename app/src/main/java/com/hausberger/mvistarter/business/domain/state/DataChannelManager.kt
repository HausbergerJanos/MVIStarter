package com.hausberger.mvistarter.business.domain.state

import android.util.Log
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*

abstract class DataChannelManager<ViewState> {

    private val dataChannel = MutableSharedFlow<DataState<ViewState>>(extraBufferCapacity = 10)
    private var job: Job

    private var channelScope: CoroutineScope? = null
    private val stateEventManager: StateEventManager = StateEventManager()

    val messageStack = MessageStack()
    val shouldDisplayProgressBar = stateEventManager.shouldDisplayProgressBar

    init {
        Log.d("FLOW_MANAGER-->", "setup channel")
        job = dataChannel
            .onEach { dataState ->
                withContext(Main) {
                    dataState.data?.let { viewState ->
                        Log.d("FLOW_MANAGER-->", "Handle new data in FlowManager")
                        handleNewData(viewState)
                    }
                    dataState.stateMessage?.let { stateMessage ->
                        handleNewStateMessage(stateMessage)
                    }
                    dataState.stateEvent?.let { stateEvent ->
                        removeStateEvent(stateEvent)
                    }
                }
            }
            .launchIn(getChannelScope())
    }

    fun launchJob (
        stateEvent: StateEvent,
        dataStateFlow: Flow<DataState<ViewState>?>
    ) {
        if (!isStateEventActive(stateEvent)) {
            println("CHANNEL--> Launching job: ${stateEvent.eventName()}")
            addStateEvent(stateEvent)
            dataStateFlow
                .onEach { dataState ->
                    dataState?.let { safeDataState ->
                        Log.d("FLOW_MANAGER-->", "Offer data in FlowManager")
                        offerToDataChannel(safeDataState)
                    }
                }
                .launchIn(getChannelScope())
        }
    }

    private fun offerToDataChannel(dataState: DataState<ViewState>) {
        dataChannel.apply {
            tryEmit(dataState)
        }
    }

    abstract fun handleNewData(data: ViewState)

    private fun handleNewStateMessage(stateMessage: StateMessage) {
        appendStateMessage(stateMessage)
    }

    private fun appendStateMessage(stateMessage: StateMessage) {
        messageStack.add(stateMessage)
    }

    fun removeStateEvent(stateEvent: StateEvent?) = stateEventManager.removeStateEvent(stateEvent)

    private fun addStateEvent(stateEvent: StateEvent) = stateEventManager.addStateEvent(stateEvent)

    private fun isStateEventActive(stateEvent: StateEvent) =
        stateEventManager.isStateEventActive(stateEvent)

    private fun getChannelScope(): CoroutineScope {
        return channelScope ?: setupNewChannelScope(CoroutineScope(Dispatchers.IO))
    }

    private fun setupNewChannelScope(coroutineScope: CoroutineScope): CoroutineScope {
        channelScope = coroutineScope
        return channelScope as CoroutineScope
    }

    private fun isMessageStackEmpty(): Boolean {
        return messageStack.isStackEmpty()
    }

    fun clearStateMessage(index: Int = 0) = messageStack.removeAt(index)

    // for debugging
    fun getActiveJobs() = stateEventManager.getActiveJobNames()

    fun isJobAlreadyActive(stateEvent: StateEvent): Boolean {
        return isStateEventActive(stateEvent)
    }

    fun clearActiveStateEventCounter() = stateEventManager.clearActiveStateEventCounter()

    fun clearAllStateMessages() = messageStack.clear()

    fun printStateMessages() {
        for (message in messageStack) {
            println("${message.response.message}")
        }
    }

    fun cancelJobs() {
        channelScope?.let { safeChannelScope ->
            if (safeChannelScope.isActive) {
                safeChannelScope.cancel()
            }

            channelScope = null
        }

        clearActiveStateEventCounter()
    }
}