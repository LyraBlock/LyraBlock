package app.lyrablock.util

object AbuseBoolean {
    fun Boolean.toInt() = when (this) {
        true -> 1
        false -> 0
    }

    operator fun Boolean.times(other: Int) = when(this) {
        true -> other
        false -> 0
    }

    operator fun Boolean.times(other: Double) = when(this) {
        true -> other
        false -> 0.0
    }
}
