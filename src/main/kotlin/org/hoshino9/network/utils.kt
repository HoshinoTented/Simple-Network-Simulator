package org.hoshino9.network

import java.lang.Exception

fun checkCreated(status: Status): Status {
    check(status === Status.Created) {
        "Network has been Started or already Dead"
    }

    return Status.Started
}

fun checkStarted(status: Status) {
    check(status === Status.Started) {
        "Network isn't Started or already Dead"
    }
}

fun unreachable(): Nothing {
    throw Exception("Unreachable")
}