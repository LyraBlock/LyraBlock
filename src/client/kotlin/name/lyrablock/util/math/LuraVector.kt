package name.lyrablock.util.math

import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import org.joml.Vector3f
import kotlin.math.sqrt

data class LuraVector(val x: Double, val y: Double, val z: Double) {
    fun add(x: Number, y: Number, z: Number) =
        LuraVector(this.x + x.toDouble(), this.y + y.toDouble(), this.z + z.toDouble())

    operator fun plus(that: LuraVector) = LuraVector(x + that.x, y + that.y, z + that.z)

    operator fun unaryMinus() = LuraVector(-x, -y, -z)

    operator fun minus(that: LuraVector) = this + (-that)

    operator fun times(that: Number) = LuraVector(that.toDouble() * x, that.toDouble() * y, that.toDouble() * y)

    operator fun div(that: Number) = LuraVector(x / that.toDouble(), y / that.toDouble(), z / that.toDouble())

    infix fun dot(that: LuraVector) = x * that.x + y * that.y + z * that.z

    infix fun cross(that: LuraVector): LuraVector {
        val (u, v, w) = that
        return LuraVector(y * w - z * v, z * u - x * w, x * v - y * u)
    }

    fun squared() = this dot this

    val length: Double get() = sqrt(squared())

    fun distanceTo(that: LuraVector) = (this - that).length

    fun normalized() = this / length

    fun toVec3d() = Vec3d(x, y, z)

    companion object {
        fun Number.times(that: LuraVector) = that * this

        fun of(x: Number, y: Number, z: Number) = LuraVector(x.toDouble(), y.toDouble(), z.toDouble())

        fun of(that: Vec3d) = LuraVector(that.x, that.y, that.z)

        fun of(that: Vec3i) = of(that.x, that.y, that.z)

        fun of(that: Vector3f) = of(that.x, that.y, that.z)

    }
}