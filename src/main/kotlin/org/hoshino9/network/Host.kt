package org.hoshino9.network

import kotlinx.coroutines.CoroutineScope
import org.hoshino9.network.impl.HostImpl

interface Host<Ip, P> : CoroutineScope {
    val ip: Ip
    val network: Network<Ip, P>
    val status: Status

    /**
     * 在 [Network.start] 被调用时，[Host.onConfigure] 被调用后调用，并且仅调用一次
     */
    suspend fun start()

    /**
     * 在被添加到 [Network] 中后，[Network.start] 时被调用，并且仅调用一次
     */
    suspend fun onConfigure(network: Network<Ip, P>, pipe: NetworkPipe<Ip, P>)

    suspend fun sendTo(dest: Ip, content: P)
    suspend fun receive(): Packet<Ip, P>?
}

fun <Ip, P> Host(ip: Ip, program: suspend Host<Ip, P>.() -> Unit): Host<Ip, P> {
    return HostImpl(ip, program)
}