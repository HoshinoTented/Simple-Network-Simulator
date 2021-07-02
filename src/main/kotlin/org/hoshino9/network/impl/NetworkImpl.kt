package org.hoshino9.network.impl

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.hoshino9.network.*
import kotlin.coroutines.CoroutineContext

internal class NetworkImpl<Ip, P> : Network<Ip, P> {
    override var status: Status = Status.Created

    private var hosts: Map<Host<Ip, P>, Pipe<Packet<Ip, P>>?> = HashMap()
    private var job: Job? = null

    private suspend fun forward(packet: Packet<Ip, P>) {
        val dest = hosts.keys.find { it.ip == packet.destination } ?: return        // drop packet
        val pipe = hosts[dest] ?: TODO("unreachable")

        pipe.send(packet)
    }

    private fun _start() {
        this.job = this.launch {
            for ((host, _pipe) in hosts) {
                val pipe = _pipe ?: TODO("unreachable")
                val packet = pipe.receive() ?: continue

                forward(packet)
            }
        }
    }

    override suspend fun start() {
        this.status = checkForStart(status)

        hosts = hosts.mapValues { (k, _) ->
            val pipe = Pipe<Packet<Ip, P>>()

            k.onConfigure(this, pipe.reverse())
            k.start()

            pipe
        }

        _start()
    }

    override fun add(host: Host<Ip, P>) {
        checkForStart(status)

        check(! hosts.containsKey(host)) {
            "The Host is already added to this Network"
        }

        hosts = hosts.plus(host to null)
    }

    override suspend fun join() {
        checkStarted(status)

        job?.join() ?: TODO("unreachable")
    }
}