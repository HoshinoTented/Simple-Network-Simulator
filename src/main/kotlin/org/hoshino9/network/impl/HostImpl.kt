package org.hoshino9.network.impl

import kotlinx.coroutines.launch
import org.hoshino9.network.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

internal class HostImpl<Ip, P>(override val ip: Ip, val program: suspend Host<Ip, P>.() -> Unit) : Host<Ip, P> {
    override val coroutineContext: CoroutineContext
        get() = EmptyCoroutineContext

    override lateinit var network: Network<Ip, P>
    override var status: Status = Status.Created

    private lateinit var pipe: NetworkPipe<Ip, P>

    override suspend fun start() {
        this.status = checkForStart(status)

        launch {
            program()
            status = Status.Dead        // do multithreading safety
        }
    }

    override suspend fun onConfigure(network: Network<Ip, P>, pipe: NetworkPipe<Ip, P>) {
        this.network = network
        this.pipe = pipe
    }

    override suspend fun sendTo(dest: Ip, content: P) {
        val packet = SimplePacket(ip, dest, false, content)

        pipe.send(packet)
    }

    override suspend fun receive(): Packet<Ip, P>? {
        return pipe.receive()
    }
}