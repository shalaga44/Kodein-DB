package org.kodein.db.impl.utils


class PhonyLock private constructor() {
    companion object {
        internal val instance = PhonyLock()
    }
}

actual typealias Lock = PhonyLock

internal actual fun newLock(): Lock = PhonyLock.instance

internal actual inline fun <T> Lock.withLock(action: () -> T): T = action()

actual typealias RWLock = PhonyLock

internal actual fun newRWLock(): Lock = PhonyLock.instance

internal actual inline fun <T> RWLock.write(action: () -> T): T = action()

internal actual inline fun <T> RWLock.read(action: () -> T): T = action()
