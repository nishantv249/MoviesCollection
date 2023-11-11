package com.nishant.moviescollection.il.util

fun <T> ArrayDeque<T>.push(t : T) = addLast(t)

fun <T> ArrayDeque<T>.pop() = removeLast()