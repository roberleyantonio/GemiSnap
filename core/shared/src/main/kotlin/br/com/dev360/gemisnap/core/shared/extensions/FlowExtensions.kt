package br.com.dev360.gemisnap.core.shared.extensions

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest

suspend fun <T> Flow<T>.collectLatestSafety(function: (T) -> Unit) {
    this.catch {
        /* Noting to do here */
    }.collectLatest { function(it) }
}