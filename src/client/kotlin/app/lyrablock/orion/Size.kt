package app.lyrablock.orion

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

    companion object {
        val ZERO = Size(0, 0)
        val INFINITY = Size(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY)
    }
}
