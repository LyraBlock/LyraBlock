package app.lyrablock.util

import kotlinx.serialization.Serializable

@Serializable
data class TripleInt(val first: Int, val second: Int, val third: Int) {
    fun toTriple() = Triple(first, second, third)

    operator fun get(index: Int): Int = when(index) {
        0 -> first
        1 -> second
        2 -> third
        else -> throw NoSuchElementException()
    }

    companion object {
        fun triple(value: Int) = TripleInt(value, value, value)
    }
}
