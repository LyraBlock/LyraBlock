package app.lyrablock.orion.math

import app.lyrablock.orion.math.AffineTransformation.Companion.IDENTITY

class AffineStack(val maxSize: Int) {
    val stack = ArrayDeque<AffineTransformation>()

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

    fun peek(): AffineTransformation {
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


    fun translate(tx: Number, ty: Number) : AffineStack {
        val current = stack.removeLast()
        val translationMatrix = IDENTITY.copy(
            tx = tx.toFloat(),
            ty = ty.toFloat()
        )

        val newMatrix = current * translationMatrix

        stack.addLast(newMatrix)

        return this
    }

    fun scale(sx: Number, sy: Number) : AffineStack {
        val current = stack.removeLast()

        val scaleMatrix = AffineTransformation(
            sx.toFloat(), 0, 0,
            0, sy.toFloat(), 0
        )

        val newMatrix = current * scaleMatrix

        stack.addLast(newMatrix)

        return this
    }

    fun scale(s: Number) = scale(s, s)
}
