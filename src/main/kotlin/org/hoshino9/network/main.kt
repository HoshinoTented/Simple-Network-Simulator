package org.hoshino9.network

import kotlinx.coroutines.isActive

suspend fun main() {
    val network = Network<Ipv0, Int, String>()
    val host0 = Host<Ipv0, Int, String>(Ipv0(0)) {
        sendTo(114, Socket(Ipv0(1), 514), "Hello")
        println("Host0 Dead")
    }

    val host1 = Host<Ipv0, Int, String>(Ipv0(1)) {
        while (true) {
            val packet = receive() ?: continue
            val source = packet.source
            val content = packet.decode() ?: continue

            println("from ${source.ip}:${source.port}")
            println(content)
            println("Host1 Dead")

            break
        }
    }

    network.register(host1)
    network.register(host0)

    network.start()

    network.join()
}