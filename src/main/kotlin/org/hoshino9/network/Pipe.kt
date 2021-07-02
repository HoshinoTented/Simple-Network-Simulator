package org.hoshino9.network

import org.hoshino9.network.impl.PipeImpl

interface Pipe<T> {
    suspend fun send(element: T)
    suspend fun receive(): T?

    fun reverse(): Pipe<T>
}

fun <T> Pipe(): Pipe<T> = PipeImpl()