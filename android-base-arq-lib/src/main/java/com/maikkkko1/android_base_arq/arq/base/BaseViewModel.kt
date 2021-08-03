package com.maikkkko1.android_base_arq.arq.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maikkkko1.android_base_arq.arq.common.SingleLiveEvent
import com.maikkkko1.android_base_arq.arq.common.UserInteraction
import com.maikkkko1.android_base_arq.arq.common.ViewCommand
import kotlinx.coroutines.launch


abstract class BaseViewModel : ViewModel() {
    private val userFlowInteraction = mutableListOf<UserInteraction>()

    private val _commandObservable = SingleLiveEvent<ViewCommand>()
    val commandObservable: LiveData<ViewCommand> get() = _commandObservable

    /**
     * Called by view to send 'an interaction' to the view model.
     */
    fun interact(userInteraction: UserInteraction) {
        viewModelScope.launch {
            suspendedInteraction(userInteraction)
        }
    }

    /**
     * Called by view to send 'a suspended interaction' to the view model.
     * It's called when the suspended function must be connect with the view life cycle.
     */
    suspend fun suspendedInteraction(userInteraction: UserInteraction) {
        userFlowInteraction.add(userInteraction)
        handleUserInteraction(userInteraction)
    }

    protected open suspend fun handleUserInteraction(interaction: UserInteraction) = Unit

    protected open suspend fun sendCommand(viewCommand: ViewCommand) {
        _commandObservable.value = viewCommand
    }

    fun reprocessLastInteraction() {
        viewModelScope.launch {
            handleUserInteraction(userFlowInteraction.last())
        }
    }

    suspend fun suspendedreprocessLastInteraction() {
        handleUserInteraction(userFlowInteraction.last())
    }

    fun <T : UserInteraction> hasInteractionOnFlow(item: T): Boolean {
        return userFlowInteraction.firstOrNull() {
            it.javaClass == item.javaClass
        } != null
    }

    fun <T : UserInteraction> hasMoreInteractionOnFlowThen(item: T, count: Int): Boolean {
        return userFlowInteraction.count {
            it.javaClass == item.javaClass
        } > count
    }

    override fun onCleared() {
        userFlowInteraction.clear()
        super.onCleared()
    }
}