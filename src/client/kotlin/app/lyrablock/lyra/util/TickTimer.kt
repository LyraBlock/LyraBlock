package app.lyrablock.lyra.util

class TickTimer {
    var ticks: Long = 0L
    // The (interval, callback) pair.
    private val intervalEvents = mutableListOf<Pair<Int, () -> Unit>>()

    fun tick() {
        ++ticks

        intervalEvents.forEach {
            val (interval, callback) = it
            if (ticks % interval == 0L) {
                callback.invoke()
            }
        }
    }

    fun reset() {
        ticks = 0L
    }

    fun onInterval(interval: Int, callback: () -> Unit) {
        intervalEvents.add(interval to callback)
    }

    fun everySecond(callback: () -> Unit) = onInterval(20, callback)
}
