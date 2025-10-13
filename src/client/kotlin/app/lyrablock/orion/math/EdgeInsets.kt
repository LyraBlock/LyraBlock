package app.lyrablock.orion.math

class EdgeInsets(val top: Int = 0, val bottom: Int = 0, val left: Int = 0, val right: Int = 0) {
    val horizontal get() = left + right
    val vertical get() = top + bottom

    companion object {
        val NONE = all(0)

        fun symmetric(x: Int = 0, y: Int = 0) = EdgeInsets(top = y, bottom = y, left = x, right = x)
        fun all(amount: Int = 0) = EdgeInsets(amount, amount, amount, amount)
    }
}
