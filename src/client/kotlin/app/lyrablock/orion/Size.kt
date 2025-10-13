package app.lyrablock.orion

import net.minecraft.client.gui.ScreenRect

data class Size(val width: Int, val height: Int) {
    constructor(width: Number, height: Number) : this(width.toInt(), height.toInt())

//    fun toInt() = width.toInt() to height.toInt()

    fun coerceIn(min: Size, max: Size) = Size(
        width.coerceIn(min.width, max.width),
        height.coerceIn(min.height, max.height)
    )

    fun coerceIn(constraints: Constraints) = coerceIn(constraints.min, constraints.max)

    infix fun intersect(other: Size) = Size(
        width.coerceAtMost(other.width),
        height.coerceAtMost(other.height)
    )

    fun atScreenOrigin() = ScreenRect(0, 0, width, height)

    companion object {
        fun square(a: Number) = Size(a, a)

        val ZERO = Size(0, 0)
        val INFINITY = square(Int.MAX_VALUE)
    }
}
