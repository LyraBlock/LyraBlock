package app.lyrablock.util.render

import net.minecraft.client.util.math.MatrixStack
import org.joml.Matrix3x2fStack

@Suppress("unused")
object MatrixStackDSL {
    fun Matrix3x2fStack.scale(x: Number, y: Number, z: Number = 1): Matrix3x2fStack {
        scale(x.toFloat(), y.toFloat())
        return this
    }

    fun Matrix3x2fStack.translate(x: Number, y: Number, z: Number = 0): Matrix3x2fStack {
        translate(x.toFloat(), y.toFloat())
        return this
    }

    fun Matrix3x2fStack.uniformScale3D(s: Number) = scale(s, s, s)

    fun Matrix3x2fStack.uniformScale2D(s: Number) = scale(s, s)

    fun MatrixStack.a() {}
}
