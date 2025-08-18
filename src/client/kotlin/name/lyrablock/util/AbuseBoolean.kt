package name.lyrablock.util

object AbuseBoolean {
    fun Boolean.toInt() = when (this) {
        true -> 1
        false -> 0
    }
}
