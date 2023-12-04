package com.nishant.il.il.util

fun <T> ArrayDeque<T>.push(t : T) = addLast(t)

fun <T> ArrayDeque<T>.pop() = removeLast()