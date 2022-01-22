package com.hausberger.mvistarter.business.domain.state

import com.hausberger.mvistarter.util.printLogD
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.*

abstract class DataFlowManager<ViewState> {
    // A hot Flow that shares emitted DataState values among all its collectors in a broadcast fashion,
    // so that all collectors get all emitted values
    private val dataFlow by lazy { MutableSharedFlow<DataState<ViewState>>(extraBufferCapacity = 5) }

    // Makes possible to cancel running coroutine anytime. Cancellation or failure of any child
    // coroutine in this scope cancels all the other children
    private var scope: CoroutineScope? = null

    // Keeps track of active StateEvents
    private val stateEventManager: StateEventManager = StateEventManager()

    //  Keeps track of whether the progress bar should show or not
    val shouldDisplayProgressBar = stateEventManager.shouldDisplayProgressBar

    // Keeps track of StateMessages
    val messageStack = MessageStack()

    init {
        printLogD("DataFlowManager", "Setup!")
        collectDataFlow()
    }

    /**
     * Launch [dataFlow] by collect terminal operator. Handle new [ViewState], [StateMessage] and
     * progressBar visibility on **[Main]** dispatcher
     */
    private fun collectDataFlow() {
        dataFlow
            .onEach { dataState ->
                withContext(Main) {
                    dataState.data?.let { viewState ->
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
            .launchIn(getScope())
    }

    /**
     * If [StateEvent] not active yet start collecting [DataState] flow
     * @param stateEvent Actual [StateEvent]
     * @param dataStateFlow Prepared [DataState] flow which will be collected
     */
    fun collectFlow(
        stateEvent: StateEvent,
        dataStateFlow: Flow<DataState<ViewState>?>
    ) {
        if (!isStateEventActive(stateEvent)) {
            // StateEvent is not active yet, so dataStateFlow can be collected
            printLogD("DataFlowManager", "Launching job: ${stateEvent.eventName()}")
            addStateEvent(stateEvent)
            dataStateFlow
                .onEach { dataState ->
                    dataState?.let { safeDataState ->
                        emitIntoDataFlow(safeDataState)
                    }
                }
                .launchIn(getScope())
        }
    }

    /**
     * Checks if the state event is active
     */
    private fun isStateEventActive(stateEvent: StateEvent) =
        stateEventManager.isStateEventActive(stateEvent)

    /**
     * Add [StateEvent] into active events map. It will mark [StateEvent] as an active event
     */
    private fun addStateEvent(stateEvent: StateEvent) = stateEventManager.addStateEvent(stateEvent)

    /**
     * Try to emit data into [dataFlow] SharedFlow without suspending it
     */
    private fun emitIntoDataFlow(dataState: DataState<ViewState>) {
        printLogD("DataFlowManager", "Offer data in FlowManager")
        dataFlow.apply {
            tryEmit(dataState)
        }
    }

    /**
     * An abstract method which will be notify about [ViewState] change
     */
    abstract fun handleNewData(data: ViewState)

    private fun handleNewStateMessage(stateMessage: StateMessage) {
        appendStateMessage(stateMessage)
    }

    private fun appendStateMessage(stateMessage: StateMessage) {
        messageStack.add(stateMessage)
    }

    fun removeStateEvent(stateEvent: StateEvent?) = stateEventManager.removeStateEvent(stateEvent)

    private fun getScope(): CoroutineScope {
        return scope ?: setupNewScope(CoroutineScope(Dispatchers.IO))
    }

    private fun setupNewScope(coroutineScope: CoroutineScope): CoroutineScope {
        scope = coroutineScope
        return scope as CoroutineScope
    }

    /**
     * Checks if [messageStack] contains any [StateMessage]
     * @return True if [messageStack] has no more items
     */
    private fun isMessageStackEmpty(): Boolean {
        return messageStack.isStackEmpty()
    }

    fun removeStateMessage(index: Int = 0) = messageStack.removeAt(index)

    // for debugging
    fun getActiveJobs() = stateEventManager.getActiveJobNames()

    fun clearAllStateMessages() = messageStack.clear()

    fun printStateMessages() {
        for (message in messageStack) {
            println("${message.response.message}")
        }
    }

    fun cancelJobs() {
        printLogD("DataFlowManager", "Cancel all jobs!")
        scope?.let { safeChannelScope ->
            if (safeChannelScope.isActive) {
                safeChannelScope.cancel()
            }

            scope = null
        }

        clearActiveStateEvents()
    }

    fun clearActiveStateEvents() = stateEventManager.clearActiveStateEvents()
}