package org.hoshino9.network.impl

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.hoshino9.network.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

internal class HostImpl<Ip, Port, P>(override val ip: Ip, val program: suspend Host<Ip, Port, P>.() -> Unit) : Host<Ip, Port, P> {
    override val coroutineContext: CoroutineContext
        get() = EmptyCoroutineContext

    override lateinit var network: Network<Ip, Port, P>
    override var status: Status = Status.Created

    private lateinit var pipe: NetworkPipe<Socket<Ip, Port>, P>

    override suspend fun start() {
        this.status = checkCreated(status)

        launch {
            program()
            status = Status.Dead        // do multithreading safety
        }
    }

    override suspend fun onConfigure(network: Network<Ip, Port, P>, pipe: NetworkPipe<Socket<Ip, Port>, P>) {
        checkCreated(status)

        this.network = network
        this.pipe = pipe
    }

    override suspend fun sendTo(port: Port, dest: Socket<Ip, Port>, content: P) {
        val packet = SimplePacket(Socket(ip, port), dest, false, content)

        pipe.send(packet)
    }

    override suspend fun receive(): Packet<Socket<Ip, Port>, P>? {
        return pipe.receive()
    }
}