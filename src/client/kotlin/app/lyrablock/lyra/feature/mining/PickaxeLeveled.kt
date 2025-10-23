package app.lyrablock.lyra.feature.mining

import kotlinx.serialization.Serializable

/**
 * Used to describe values for pickaxes that have 3 different levels,
 */
@Serializable
data class PickaxeLeveled(val first: Int, val second: Int, val third: Int) {
    fun toTriple() = Triple(first, second, third)

    operator fun get(index: Int): Int = when(index) {
        0 -> first
        1 -> second
        2 -> third
        else -> throw NoSuchElementException()
    }

    companion object {
        fun triple(value: Int) = PickaxeLeveled(value, value, value)
    }
}
