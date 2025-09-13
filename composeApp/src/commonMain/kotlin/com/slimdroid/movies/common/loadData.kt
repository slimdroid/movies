package com.slimdroid.movies.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn

fun <T, R> ViewModel.loadData(
    initialState: T,
    loadData: suspend FlowCollector<T>.(currentState: T) -> Unit,
    refreshMechanism: SharedFlow<R>? = null,
    timeout: Long = 5_000,
    refreshData: (suspend FlowCollector<T>.(currentState: T, refreshParams: R) -> Unit)? = null,
): StateFlow<T> {
    if (refreshMechanism != null) {
        requireNotNull(refreshData) {
            "You've provided a refresh mechanism but no way to refresh the data"
        }
    }
    if (refreshData != null) {
        requireNotNull(refreshMechanism) {
            "You've provided a refresh data but no mechanism to refresh the data"
        }
    }

    var latestValue = initialState
    return flow {
        emit(latestValue)
        loadData(latestValue)
        refreshMechanism?.collect { refreshParams ->
            if (refreshData != null) {
                refreshData(latestValue, refreshParams)
            }
        }
    }
        .distinctUntilChanged()
        .onEach {
            latestValue = it
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(timeout),
            initialValue = initialState
        )
}

fun <T> ViewModel.loadData(
    initialState: T,
    loadData: suspend FlowCollector<T>.(currentState: T) -> Unit,
    timeout: Long = 5_000,
): StateFlow<T> {
    var latestValue = initialState
    return flow {
        emit(latestValue)
        loadData(latestValue)
    }
        .onEach {
            latestValue = it
        }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(timeout),
            initialValue = initialState
        )
}