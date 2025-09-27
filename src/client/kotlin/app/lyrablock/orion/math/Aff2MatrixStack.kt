package app.lyrablock.orion.math

import app.lyrablock.orion.math.Aff2Matrix.Companion.IDENTITY

class Aff2MatrixStack(val maxSize: Int) {
    val stack = ArrayDeque<Aff2Matrix>()

    init {
        reset()
    }

    /**
     * Reset the stack with only an identity matrix left
     */
    fun reset() {
        stack.clear()
        stack.addLast(IDENTITY)
    }

    fun peek(): Aff2Matrix {
        return stack.last()
    }

    fun push() {
        if (stack.size < maxSize) {
            stack.addLast(peek())
        }
    }

    fun pop() {
        if (stack.size > 1) {
            stack.removeLast()
        }
    }


    fun translate(tx: Number, ty: Number) : Aff2MatrixStack {
        val current = stack.removeLast()
        val translationMatrix = IDENTITY.copy(
            tx = tx.toFloat(),
            ty = ty.toFloat()
        )

        val newMatrix = current * translationMatrix

        stack.addLast(newMatrix)

        return this
    }

    fun scale(sx: Number, sy: Number) : Aff2MatrixStack {
        val current = stack.removeLast()

        val scaleMatrix = Aff2Matrix(
            sx.toFloat(), 0, 0,
            0, sy.toFloat(), 0
        )

        val newMatrix = current * scaleMatrix

        stack.addLast(newMatrix)

        return this
    }

    fun scale(s: Number) = scale(s, s)
}
