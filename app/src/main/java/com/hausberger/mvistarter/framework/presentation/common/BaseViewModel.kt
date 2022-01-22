package com.hausberger.mvistarter.framework.presentation.common

import androidx.lifecycle.ViewModel
import com.hausberger.mvistarter.R
import com.hausberger.mvistarter.business.domain.state.*
import com.hausberger.mvistarter.util.printLogD
import kotlinx.coroutines.flow.*

abstract class BaseViewModel<ViewState> : ViewModel() {
    // Observable ViewState
    private val _viewState by lazy { MutableStateFlow(initNewViewState()) }
    val viewState = _viewState.asStateFlow()

    // Track progressBar visibility
    lateinit var shouldDisplayProgressBar: StateFlow<Boolean>

    // Track StateMessage which comes from DataState
    lateinit var stateMessage: StateFlow<StateMessage?>

    // Responsible to track and control ViewState changes and notify all component about it
    private lateinit var dataFlowManager: DataFlowManager<ViewState>

    init {
        setupDataFlowManager()
        doAfterFlowManagerCreated()
    }

    /**
     * Initialize an instance of [DataFlowManager] which will be notify our [ViewModel]
     * about state changes
     */
    private fun setupDataFlowManager() {
        dataFlowManager =
            object : DataFlowManager<ViewState>() {
                override fun handleNewData(data: ViewState) {
                    // Notify child about ViewState changed
                    this@BaseViewModel.handleNewData(data)
                }
            }
    }

    /**
     * Called after [DataFlowManager] created. Prepare observable [stateMessage]
     * and [shouldDisplayProgressBar] according to [DataFlowManager]
     */
    private fun doAfterFlowManagerCreated() {
        shouldDisplayProgressBar = dataFlowManager.shouldDisplayProgressBar
        stateMessage = dataFlowManager.messageStack.stateMessage
    }

    /**
     * Handle ViewState changes
     * @param ViewState The new [ViewState]
     */
    abstract fun handleNewData(data: ViewState)

    /**
     * Create a new instance of ViewState
     */
    abstract fun initNewViewState(): ViewState

    /**
     * Sets the [StateEvent] which describes the state we want the ViewState to take
     * @param stateEvent Desired state which will be trigger to perform action
     */
    abstract fun setStateEvent(stateEvent: StateEvent)

    /**
     * Launches the collection of [DataState] flow
     *
     * @param stateEvent The desired state
     * @param dataStateFlow The [DataState] flow which will be collected
     */
    fun collectFlow(
        stateEvent: StateEvent,
        dataStateFlow: Flow<DataState<ViewState>?>
    ) = dataFlowManager.collectFlow(stateEvent, dataStateFlow)

    /**
     * Creates a custom error [DataState] and emit it.
     * @param stateEvent Actual [StateEvent]
     * @param stateMessage Custom [StateMessage]
     */
    fun emitStateMessageEvent(
        stateMessage: StateMessage,
        stateEvent: StateEvent
    ): Flow<DataState<ViewState>> = flow {
        emit(
            DataState.error(
                response = stateMessage.response,
                stateEvent = stateEvent
            )
        )
    }

    /**
     * Emits an invalid [DataState] if action not defined for [StateEvent]
     * @param stateEvent Actual [StateEvent] which is not handled
     */
    fun emitInvalidStateEvent(
        stateEvent: StateEvent
    ): Flow<DataState<ViewState>> = flow {
        emit(
            DataState.error(
                response = Response(
                    message = SimpleMessage(
                        messageRes = R.string.invalid_state_event
                    ),
                    uiComponentType = UIComponentType.None,
                    messageType = MessageType.Error
                ),
                stateEvent = stateEvent
            )
        )
    }

    /**
     * Get current [ViewState]. If it is not exist yet initialize it
     */
    fun getCurrentViewStateOrNew(): ViewState {
        return viewState.value ?: initNewViewState()
    }

    /**
     * Override [ViewState] value. It will notify all subscribers
     */
    fun setViewState(viewState: ViewState) {
        _viewState.value = viewState
    }

    fun getMessageStackSize(): Int {
        return dataFlowManager.messageStack.size
    }

    fun removeStateMessage(index: Int = 0) {
        printLogD("BaseViewModel", "ClearStateMessage")
        dataFlowManager.removeStateMessage(index)
    }

    /**
     * Cancels all running jobs
     */
    fun cancelJobs() {
        dataFlowManager.cancelJobs()
    }
}