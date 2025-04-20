package name.lyrablock.util.math

import net.minecraft.util.math.Vec3d
import net.minecraft.util.math.Vec3i
import org.joml.Vector3f
import kotlin.math.sqrt

data class Vector3d(val x: Double, val y: Double, val z: Double) {
    fun add(x: Number, y: Number, z: Number) =
        Vector3d(this.x + x.toDouble(), this.y + y.toDouble(), this.z + z.toDouble())

    operator fun plus(that: Vector3d) = Vector3d(x + that.x, y + that.y, z + that.z)

    operator fun unaryMinus() = Vector3d(-x, -y, -z)

    operator fun minus(that: Vector3d) = this + (-that)

    operator fun times(that: Number) = Vector3d(that.toDouble() * x, that.toDouble() * y, that.toDouble() * y)

    operator fun div(that: Number) = Vector3d(x / that.toDouble(), y / that.toDouble(), z / that.toDouble())

    infix fun dot(that: Vector3d) = x * that.x + y * that.y + z * that.z

    infix fun cross(that: Vector3d): Vector3d {
        val (u, v, w) = that
        return Vector3d(y * w - z * v, z * u - x * w, x * v - y * u)
    }

    fun squared() = this dot this

    val length: Double get() = sqrt(squared())

    fun distanceTo(that: Vector3d) = (this - that).length

    fun normalized() = this / length

    fun toVec3d() = Vec3d(x, y, z)

    companion object {
        fun Number.times(that: Vector3d) = that * this

        fun of(x: Number, y: Number, z: Number) = Vector3d(x.toDouble(), y.toDouble(), z.toDouble())

        fun of(that: Vec3d) = Vector3d(that.x, that.y, that.z)

        fun of(that: Vec3i) = of(that.x, that.y, that.z)

        fun of(that: Vector3f) = of(that.x, that.y, that.z)

    }
}