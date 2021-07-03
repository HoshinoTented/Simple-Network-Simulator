package org.hoshino9.network.impl

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.receiveAsFlow
import org.hoshino9.network.Pipe

internal class PipeImpl<T>(
    val l2r: Channel<T> = Channel(16),
    val r2l: Channel<T> = Channel(16)) : Pipe<T> {

    override suspend fun send(element: T) {
        l2r.trySend(element)
    }

    override suspend fun receive(): T? {
        return r2l.tryReceive().getOrNull()
    }

    override fun reverse(): Pipe<T> {
        return PipeImpl(r2l, l2r)
    }
}