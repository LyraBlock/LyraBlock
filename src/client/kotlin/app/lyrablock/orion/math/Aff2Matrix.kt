package app.lyrablock.orion.math

/**
 * A matrix of class $Aff(2)$ (2D affine group),
 * consisting of a 2D linear transformation and a translation.
 */
data class Aff2Matrix(
    val i1: Float,
    val j1: Float,
    val tx: Float,
    val i2: Float,
    val j2: Float,
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
            i1 * other.i1 + j1 * other.i2, i1 * other.j1 + j1 * other.j2, i1 * other.tx + j1 * other.ty + tx,
            i2 * other.i1 + j2 * other.i2, i2 * other.j1 + j2 * other.j2, i2 * other.tx + j2 * other.ty + ty
        )
    }

    operator fun plus(other: Aff2Matrix): Aff2Matrix {
        return Aff2Matrix(
            i1 + other.i1, j1 + other.j1, tx + other.tx,
            i2 + other.i2, j2 + other.j2, ty + other.ty
        )
    }

    operator fun unaryMinus() = Aff2Matrix(
        -i1, -j1, -tx,
        -i2, -j2, -ty
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
