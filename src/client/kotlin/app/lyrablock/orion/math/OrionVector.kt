package app.lyrablock.orion.math

data class OrionVector(val x: Float, val y: Float) {
    constructor(x: Number, y: Number) : this(x.toFloat(), y.toFloat())

    fun toInt() = x.toInt() to y.toInt()

    operator fun plus(other: OrionVector) = OrionVector(x + other.x, y + other.y)
    operator fun unaryMinus() = OrionVector(-x, -y)
    operator fun minus(other: OrionVector) = this + (-other)
}
