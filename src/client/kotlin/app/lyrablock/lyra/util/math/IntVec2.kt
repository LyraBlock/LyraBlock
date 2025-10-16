package app.lyrablock.lyra.util.math

import org.joml.Vector2i

data class IntVec2(val x: Int, val y: Int) {
    operator fun plus(other: IntVec2) = IntVec2(x + other.x, y + other.y)
    operator fun unaryMinus() = IntVec2(-x, -y)
    operator fun minus(other: IntVec2) = this + (-other)

    companion object {
        fun of(pair: Pair<Int, Int>) = IntVec2(pair.first, pair.second)
        // JOML mutable. This immutable. This good.
        fun of(vec: Vector2i) = IntVec2(vec.x, vec.y)
    }
}
