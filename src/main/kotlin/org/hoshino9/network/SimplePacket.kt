package org.hoshino9.network

class SimplePacket<Ip, P>(override val source: Ip, override val destination: Ip, private val corrupted: Boolean, private val content: P) : Packet<Ip, P> {
    override fun decode(): P? {
        return if (corrupted) {
            null
        } else {
            content
        }
    }
}