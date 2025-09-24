package app.lyrablock.lyra.util.render

import org.joml.Matrix3x2fStack

@Suppress("unused")
object MatrixStackDSL {
    @Deprecated("z no longer needed")
    fun Matrix3x2fStack.scale(x: Number, y: Number, z: Number): Matrix3x2fStack = scale(x, y)

    fun Matrix3x2fStack.scale(x: Number, y: Number): Matrix3x2fStack {
        scale(x.toFloat(), y.toFloat())
        return this
    }

    @Deprecated("z no longer needed")
    fun Matrix3x2fStack.translate(x: Number, y: Number, z: Number): Matrix3x2fStack = translate(x,y)

    fun Matrix3x2fStack.translate(x: Number, y: Number): Matrix3x2fStack {
        translate(x.toFloat(), y.toFloat())
        return this
    }

    fun Matrix3x2fStack.uniformScale(s: Number) = scale(s, s) }
