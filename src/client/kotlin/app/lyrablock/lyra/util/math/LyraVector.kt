package app.lyrablock.lyra.util.math

import net.minecraft.core.Vec3i
import net.minecraft.world.phys.Vec3
import org.joml.Vector3f
import java.lang.Math.toRadians
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

@Suppress("unused")
data class LyraVector(val x: Double, val y: Double, val z: Double) {
    fun add(x: Number, y: Number, z: Number) =
        LyraVector(this.x + x.toDouble(), this.y + y.toDouble(), this.z + z.toDouble())

    operator fun plus(that: LyraVector) = LyraVector(x + that.x, y + that.y, z + that.z)

    operator fun unaryMinus() = LyraVector(-x, -y, -z)

    operator fun minus(that: LyraVector) = this + (-that)

    operator fun times(that: Number) = LyraVector(that.toDouble() * x, that.toDouble() * y, that.toDouble() * y)

    operator fun div(that: Number) = LyraVector(x / that.toDouble(), y / that.toDouble(), z / that.toDouble())

    infix fun dot(that: LyraVector) = x * that.x + y * that.y + z * that.z

    infix fun cross(that: LyraVector): LyraVector {
        val (u, v, w) = that
        return LyraVector(y * w - z * v, z * u - x * w, x * v - y * u)
    }

    fun squared() = this dot this

    val length: Double get() = sqrt(squared())

    fun distanceTo(that: LyraVector) = (this - that).length

    fun normalized() = this / length

    fun toVec3d() = Vec3(x, y, z)

    fun toVector3f() = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())

    fun decompose(basis1: LyraVector, basis2: LyraVector, basis3: LyraVector): LyraVector {
        val u = this

        // Compute the determinant of the matrix A = [basis1, basis2, basis3]
        val det = basis1 dot (basis2 cross basis3)
        if (det == 0.0) {
            throw IllegalArgumentException("Basis vectors are linearly dependent")
        }

        // Use Cramer's rule for vector decomposition
        val alpha = (u dot (basis2 cross basis3)) / det
        val beta  = (basis1 cross u dot basis3) / det
        val gamma = (basis1 dot (basis2 cross u)) / det

        return LyraVector(alpha, beta, gamma)
    }

    companion object {
        fun Number.times(that: LyraVector) = that * this

        fun of(x: Number, y: Number, z: Number) = LyraVector(x.toDouble(), y.toDouble(), z.toDouble())

        fun of(that: Vec3) = LyraVector(that.x, that.y, that.z)

        fun of(that: Vec3i) = of(that.x, that.y, that.z)

        fun of(that: Vector3f) = of(that.x, that.y, that.z)

        /**
         * Returns the direction vector from the pitch and yaw (in degree).
         */
        fun directionFromPolar(pitch: Double, yaw: Double): LyraVector {
            // learn math ok? im not teaching you
            val radYaw = toRadians(-yaw)
            val radPitch = toRadians(-pitch)
            val cosPitch = cos(radPitch)
            return LyraVector(sin(radYaw) * cosPitch, sin(radPitch), cos(radYaw) * cosPitch)
        }

        fun directionFromPolar(pitch: Float, yaw: Float) = directionFromPolar(pitch.toDouble(), yaw.toDouble())

        val UNIT_CUBE = LyraVector(1.0, 1.0, 1.0)
        val UNIT_I = LyraVector(1.0, 0.0, 0.0)
        val UNIT_J = LyraVector(0.0, 1.0, 0.0)
        val UNIT_K = LyraVector(0.0, 0.0, 1.0)
        val ORIGIN = LyraVector(0.0, 0.0, 0.0)
    }
}
