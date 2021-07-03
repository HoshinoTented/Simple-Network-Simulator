package org.hoshino9.network

import org.hoshino9.network.impl.SocketImpl

interface Socket<Ip, Port> {
    val ip: Ip
    val port: Port
}

fun <Ip, Port> Socket(ip: Ip, port: Port): Socket<Ip, Port> = SocketImpl(ip, port)