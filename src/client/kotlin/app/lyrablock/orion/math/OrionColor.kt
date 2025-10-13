package app.lyrablock.orion.math

@Suppress("unused")
class OrionColor
private constructor(val red: Int, val green: Int, val blue: Int, val alpha: Int) {
    val argb get(): Int = (red shl 16) or (green shl 8) or blue or (alpha shl 24)

    val redF get() = red / 255f
    val greenF get() = green / 255f
    val blueF get() = blue / 255f
    val alphaF get() = alpha / 255f

    fun withAlpha(newAlpha: Int) = OrionColor(red, green, blue, newAlpha)
    fun withAlpha(newAlpha: Float) = withAlpha((newAlpha * 255).toInt())

    fun asGradient() = Gradient(this, this)

    companion object {
        val WHITE = OrionColor(255, 255, 255, 255)
        val BLACK = OrionColor(0, 0, 0, 255)

        fun rgba(red: Int, green: Int, blue: Int, alpha: Int = 255) = OrionColor(red, green, blue, alpha)

        fun argb(alpha: Int, red: Int, green: Int, blue: Int) = OrionColor(red, green, blue, alpha)
        fun argb(color: Int) = argb(
            (color shr 24) and 0xFF, (color shr 16) and 0xFF, (color shr 8) and 0xFF, color and 0xFF
        )
        fun argb(color: Long) = argb(color.toInt())
        fun rgb(color: Int) = argb(color + 0xFF000000.toInt())

        fun luma(percentage: Float) = BLACK.withAlpha(percentage)
    }
}
