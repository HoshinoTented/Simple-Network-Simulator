package org.hoshino9.network

suspend fun main() {
    val network = Network<Ipv0, String>()
    val host0 = Host<Ipv0, String>(Ipv0(0)) {
        sendTo(Ipv0(1), "Hello")
        println("Host0 Dead")
    }

    val host1 = Host<Ipv0, String>(Ipv0(1)) {
        while (true) {
            val packet = receive() ?: continue
            val content = packet.decode() ?: continue

            println(content)
            println("Host1 Dead")
        }
    }

    network.add(host0)
    network.add(host1)

    network.start()

    network.join()
}