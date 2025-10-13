package app.lyrablock.orion.math

/**
 * A matrix of class Aff(2) (2D affine group),
 * consisting of a 2D linear transformation and a translation.
 */
@Suppress("unused")
data class Aff2Matrix(
    val ix: Float,
    val jx: Float,
    val tx: Float,
    val iy: Float,
    val jy: Float,
    val ty: Float
) {
    constructor(a: Number, b: Number, c: Number, d: Number, e: Number, f: Number) : this(
        a.toFloat(),
        b.toFloat(),
        c.toFloat(),
        d.toFloat(),
        e.toFloat(),
        f.toFloat()
    )

    // Matrix multiplication bro
    operator fun times(other: Aff2Matrix): Aff2Matrix {
        return Aff2Matrix(
            ix * other.ix + jx * other.iy, ix * other.jx + jx * other.jy, ix * other.tx + jx * other.ty + tx,
            iy * other.ix + jy * other.iy, iy * other.jx + jy * other.jy, iy * other.tx + jy * other.ty + ty
        )
    }

    operator fun times(other: OrionVector): OrionVector {
        return OrionVector(
            ix * other.x + jx * other.x + tx,
            iy * other.x + jy * other.x + ty
        )
    }

    operator fun plus(other: Aff2Matrix): Aff2Matrix {
        return Aff2Matrix(
            ix + other.ix, jx + other.jx, tx + other.tx,
            iy + other.iy, jy + other.jy, ty + other.ty
        )
    }

    operator fun unaryMinus() = Aff2Matrix(
        -ix, -jx, -tx,
        -iy, -jy, -ty
    )

    operator fun minus(other: Aff2Matrix) = this + (-other)

    val transition get() = tx to ty


    companion object {
        val IDENTITY = Aff2Matrix(
            1, 0, 1,
            0, 1, 1
        )
    }
}
