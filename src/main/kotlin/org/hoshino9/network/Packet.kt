package org.hoshino9.network

interface Packet<Ip, T> {
    val source: Ip
    val destination: Ip

    /**
     * 解码当前报文，如果返回 null 则为破损（corrupt）
     */
    fun decode(): T?
}