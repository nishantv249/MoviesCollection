package com.nishant.core.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart


sealed interface LoadingState<out T> {
   data class Success<T>(val t: T) : LoadingState<T>
    data class Error(val e : String) : LoadingState<Nothing>
    object Empty  : LoadingState<Nothing>
    object Loading : LoadingState<Nothing>

}

fun <T> Flow<T>.asResult() : Flow<LoadingState<T>> {
    return map <T,LoadingState<T>> {
        LoadingState.Success(it)
    }.onStart {
       emit( LoadingState.Loading)
    }.catch {
        println("exception $it")
        emit(LoadingState.Error(it.message.toString()))
    }
}