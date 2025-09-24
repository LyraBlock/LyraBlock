package app.lyrablock.lyra.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

/**
 * A state, where you can record a time, usually representing "last edited".
 */
class TimeMarker(initialTime: Instant = Instant.DISTANT_PAST) {
    private var lastMarked: Instant = initialTime

    fun mark(time: Instant = Clock.System.now()) {
        lastMarked = time
    }

    fun untilNow() = Clock.System.now() - lastMarked
}
