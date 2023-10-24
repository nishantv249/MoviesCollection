package com.nishant.moviescollection.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed interface Result<out R> {

    object Empty : Result<Nothing>

    object Loading : Result<Nothing>

    class Error(val e: String) : Result<Nothing>

    class Success<out T>(val t : T) : Result<T>

}

fun <T> Flow<T>.asResult() : Flow<Result<T>> {
    return this.map<T,Result<T>> {
        Result.Success(it)
    }.onStart {
        emit(Result.Loading)
    }.catch {
        emit(Result.Error(it.message.toString()))
    }
}

