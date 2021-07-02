package org.hoshino9.network

import kotlinx.coroutines.CoroutineScope
import org.hoshino9.network.impl.NetworkImpl
import kotlin.coroutines.CoroutineContext

typealias NetworkPipe<Ip, T> = Pipe<Packet<Ip, T>>

interface Network<Ip, P> : CoroutineScope, CoroutineContext.Element {
    object Key : CoroutineContext.Key<Network<*, *>>

    val status: Status

    override val key: CoroutineContext.Key<*>
        get() = Key

    override val coroutineContext: CoroutineContext
        get() = this

    /**
     * 启动此网络，同时 [Network] 也应该负责启用其网络中的所有 [Host]
     */
    suspend fun start()

    /**
     * 将一个 [Host] 添加到这个 [Network] 中，并且该 [Host] 应该处于创建阶段（未被 [Host.start]）
     */
    fun add(host: Host<Ip, P>)

    suspend fun join()
}

fun <Ip, P> Network(): Network<Ip, P> {
    return NetworkImpl()
}