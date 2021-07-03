package org.hoshino9.network

import kotlinx.coroutines.CoroutineScope
import org.hoshino9.network.impl.HostImpl

interface Host<Ip, Port, P> : CoroutineScope {
    val ip: Ip
    val network: Network<Ip, Port, P>
    val status: Status

    /**
     * 在 [Network.start] 被调用时，[Host.onConfigure] 被调用后调用，并且仅调用一次
     */
    suspend fun start()

    /**
     * 在被添加到 [Network] 中后，[Network.start] 时被调用，并且仅调用一次
     */
    suspend fun onConfigure(network: Network<Ip, Port, P>, pipe: NetworkPipe<Socket<Ip, Port>, P>)

    suspend fun sendTo(port: Port, dest: Socket<Ip, Port>, content: P)
    suspend fun receive(): Packet<Socket<Ip, Port>, P>?
}

fun <Ip, Port, P> Host(ip: Ip, program: suspend Host<Ip, Port, P>.() -> Unit): Host<Ip, Port, P> {
    return HostImpl(ip, program)
}