package com.hausberger.mvistarter.business.domain.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Keeps track of active StateEvents in DataFlowManager
 *
 * Keeps track of whether the progress bar should show or not based on a boolean
 * value in each StateEvent (shouldDisplayProgressBar)
 */
class StateEventManager {

    private val activeStateEvents: HashMap<String, StateEvent> = HashMap()

    private val _shouldDisplayProgressBar by lazy { MutableStateFlow(false) }
    val shouldDisplayProgressBar = _shouldDisplayProgressBar.asStateFlow()

    fun getActiveJobNames(): MutableSet<String> {
        return activeStateEvents.keys
    }

    fun clearActiveStateEvents() {
        activeStateEvents.clear()
        syncNumActiveStateEvents()
    }

    fun addStateEvent(stateEvent: StateEvent) {
        activeStateEvents[stateEvent.eventName()] = stateEvent
        syncNumActiveStateEvents()
    }

    fun removeStateEvent(stateEvent: StateEvent?) {
        activeStateEvents.remove(stateEvent?.eventName())
        syncNumActiveStateEvents()
    }

    fun isStateEventActive(stateEvent: StateEvent): Boolean {
        activeStateEvents.keys.forEach { eventName ->
            if (stateEvent.eventName() == eventName) {
                return true
            }
        }

        return false
    }

    private fun syncNumActiveStateEvents() {
        var shouldDisplayProgressBar = false
        activeStateEvents.values.forEach { stateEvent ->
            if (stateEvent.shouldDisplayProgressBar()) {
                shouldDisplayProgressBar = true
            }
        }

        _shouldDisplayProgressBar.value = shouldDisplayProgressBar
    }
}