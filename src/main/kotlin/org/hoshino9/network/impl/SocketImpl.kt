package org.hoshino9.network.impl

import org.hoshino9.network.Socket

data class SocketImpl<Ip, Port>(
    override val ip: Ip,
    override val port: Port) : Socket<Ip, Port>