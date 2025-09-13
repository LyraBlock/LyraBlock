package name.lyrablock.util.render

import net.minecraft.client.util.math.MatrixStack

@Suppress("unused")
object MatrixStackDSL {
    fun MatrixStack.scale(x: Number, y: Number, z: Number = 1) {
        scale(x.toFloat(), y.toFloat(), z.toFloat())
    }

    fun MatrixStack.translate(x: Number, y: Number, z: Number = 0) {
        translate(x.toFloat(), y.toFloat(), z.toFloat())
    }

    fun MatrixStack.uniformScale3D(s: Number) = scale(s, s, s)

    fun MatrixStack.uniformScale2D(s: Number) = scale(s, s)

    fun MatrixStack.haveNoIdeaWhyThisExistsButItIsHereLMAO() {
        push()
        pop()
    }
}
