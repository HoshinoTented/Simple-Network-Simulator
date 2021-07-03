package org.hoshino9.network.impl

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.hoshino9.network.*

internal class NetworkImpl<Ip, Port, P> : Network<Ip, Port, P> {
    override var status: Status = Status.Created

    private var hosts: Map<Host<Ip, Port, P>, NetworkPipe<Socket<Ip, Port>, P>?> = HashMap()
    private var job: Job? = null

    private suspend fun forward(packet: Packet<Socket<Ip, Port>, P>) {
        val dest = hosts.keys.find { it.ip == packet.destination.ip } ?: return        // drop packet
        val pipe = hosts[dest] ?: unreachable()

        pipe.send(packet)
    }

    private fun _start() {
        this.job = this.launch {
            while (true) {
                var anyOnline = false

                for ((host, _pipe) in hosts) {
                    val isOnline = host.status === Status.Started

                    val pipe = _pipe ?: unreachable()
                    val packet = pipe.receive()

                    if (packet != null) {
                        forward(packet)
                    }

                    anyOnline = anyOnline or isOnline
                }

//            for ((_, _pipe) in hosts) {
//                val pipe = _pipe ?: unreachable()
//                val packet = pipe.receive() ?: continue
//
//                forward(packet)
//            }

                if (!anyOnline) break
            }

            println("Network Dead")
        }
    }

    override suspend fun start() {
        this.status = checkCreated(status)

        hosts = hosts.mapValues { (host, _) ->
            val pipe: NetworkPipe<Socket<Ip, Port>, P> = Pipe()

            host.onConfigure(this, pipe.reverse())
            host.start()

            pipe
        }

        _start()
    }

    override fun register(host: Host<Ip, Port, P>) {
        checkCreated(status)

        check(!hosts.containsKey(host)) {
            "The Host is already added to this Network"
        }

        hosts = hosts + (host to null)
    }

    override suspend fun join() {
        checkStarted(status)

        job?.join() ?: unreachable()
    }
}