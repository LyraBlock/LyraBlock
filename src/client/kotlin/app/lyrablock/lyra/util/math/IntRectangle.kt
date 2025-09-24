package app.lyrablock.lyra.util.math

@Suppress("unused")
class IntRectangle
private constructor(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    val width get() = x2 - x1
    val height get() = y2 - y1
    val start get() = x1 to y1
    val end get() = x2 to y2

    fun contains(x: Int, y: Int) = x in x1 until x2 && y in y1 until y2

    fun expand(top: Int = 0, bottom: Int = 0, left: Int = 0, right: Int = 0) = IntRectangle(x1 - left, y1 - top, x2 + right, y2 + bottom)
    fun expand(x: Int = 0, y: Int = 0) = expand(y, y, x, x)
    fun expand(inset: Int) = expand(inset, inset)

    companion object {
        fun corners(x1: Int, x2: Int, y1: Int, y2: Int) = IntRectangle(x1, y1, x2, y2)
        fun sized(x: Int, y: Int, width: Int, height: Int) = IntRectangle(x, y, x + width, y + height)
    }
}
