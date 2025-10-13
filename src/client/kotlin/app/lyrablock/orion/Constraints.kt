package app.lyrablock.orion

@Suppress("unused")
open class Constraints(val min: Size, val max: Size) {

    infix fun intersect(other: Constraints) = Constraints(
        min intersect other.min,
        max intersect other.max
    )

    /* Builders */
    fun fixed(size: Size) = this intersect Constraints(size, size)
    fun min(size: Size) = this intersect Constraints(size, Size.INFINITY)
    fun max(size: Size) = this intersect Constraints(Size.ZERO, size)
    fun minWidth(minWidth: Number) = this intersect Constraints(Size(minWidth, 0.0), Size.INFINITY)
    fun minHeight(minHeight: Number) = this intersect Constraints(Size(0.0, minHeight), Size.INFINITY)
    fun maxWidth(maxWidth: Number) = this intersect Constraints(Size.ZERO, Size(maxWidth, INF))
    fun maxHeight(maxWidth: Number) = this intersect Constraints(Size.ZERO, Size(INF, maxWidth))

    /* Methods */
    fun constrain(size: Size) = size.coerceIn(min, max)
    fun copy() = Constraints(min, max)
    fun isStrict() = min == max

    companion object : Constraints(Size.ZERO, Size.INFINITY) {
        /**
         * Does not actually constrain anything.
         */
        val NONE: Constraints = Constraints(Size.ZERO, Size.INFINITY)
        const val INF = Double.POSITIVE_INFINITY
    }
}
