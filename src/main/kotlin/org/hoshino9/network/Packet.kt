package org.hoshino9.network

interface Packet<Socket, T> {
    val source: Socket
    val destination: Socket

    /**
     * 解码当前报文，如果返回 null 则为破损（corrupt）
     */
    fun decode(): T?
}