package name.lyrablock.util.render

import name.lyrablock.util.math.LyraColor
import name.lyrablock.util.math.LyraVector
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.VertexRendering

object WorldRenderDSL {
    // The world render context is translated to the player's camera pos.
    // This function translates the matrices back to the origin.
    private inline fun WorldRenderContext.translatedToOrigin(operation: () -> Unit) {
        val matrices = this.matrixStack() ?: return
        val cameraPos = this.camera().pos

        matrices.push()
        matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z)
        operation.invoke()
        matrices.pop()
    }

    fun WorldRenderContext.renderLine(layer: RenderLayer, p1: LyraVector, p2: LyraVector, color: LyraColor) {
        val matrices = this.matrixStack() ?: return
        val consumers = this.consumers() as VertexConsumerProvider.Immediate
        val buffer = consumers.getBuffer(layer)

        translatedToOrigin {
            val matrix = matrices.peek()
            val normal = (p1 - p2).normalized().toVector3f()

            buffer.vertex(matrix, p1.toVector3f()).color(color.argb).normal(matrix, normal)
            buffer.vertex(matrix, p2.toVector3f()).color(color.argb).normal(matrix, normal)

            consumers.draw(layer)
        }

    }

    fun WorldRenderContext.renderOutline(
        layer: RenderLayer, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double, color: LyraColor
    ) {
        val matrices = this.matrixStack() ?: return

        translatedToOrigin {
            val consumers = this.consumers() as VertexConsumerProvider.Immediate
            val buffer = consumers.getBuffer(layer)

            VertexRendering.drawBox(
                matrices, buffer, x1, y1, z1, x2, y2, z2, color.redF, color.greenF, color.blueF, color.alphaF
            )
            consumers.draw(layer)
        }
    }

    fun WorldRenderContext.renderOutline(layer: RenderLayer, p1: LyraVector, p2: LyraVector, color: LyraColor) =
        renderOutline(layer, p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, color)

    fun WorldRenderContext.renderBlockOutline(layer: RenderLayer, x: Int, y: Int, z: Int, color: LyraColor) {
        val p1 = LyraVector.Companion.of(x, y, z)
        val p2 = p1 + LyraVector.Companion.UNIT_CUBE
        renderOutline(layer, p1, p2, color)
    }

    // This waypoint is left for mining waypoints, which should be integer.
    fun WorldRenderContext.renderLineFromCrosshair(layer: RenderLayer, x: Int, y: Int, z: Int, color: LyraColor) {
        // Translate to the center of the block.
        val target = LyraVector.Companion.of(x + .5, y + .5, z + .5)
        val camera = this.camera()
        val cameraPos = camera.pos

        // The position 1 block forward from the camera.
        // Since the render layer is above blocks, being translated into blocks does not matter.
        val cameraForward = LyraVector.Companion.of(cameraPos) + LyraVector.Companion.directionFromPolar(
            camera.pitch, camera.yaw
        )

        renderLine(layer, cameraForward, target, color)
    }

    /**
     * Render a filled box.
     */
    fun WorldRenderContext.renderFilled(
        layer: RenderLayer,
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        color: LyraColor
    ) {
        val matrices = this.matrixStack() ?: return

        translatedToOrigin {
            val consumers = this.consumers() ?: return
            val buffer = consumers.getBuffer(layer)

            VertexRendering.drawFilledBox(matrices, buffer, x1, y1, z1, x2, y2, z2,
                color.redF, color.greenF, color.blueF, color.alphaF
            )
        }
    }

    fun WorldRenderContext.renderFilled(
        layer: RenderLayer, p1: LyraVector, p2: LyraVector, color: LyraColor
    ) {
        renderFilled(layer, p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, color)
    }

    fun WorldRenderContext.renderBlockFilled(layer: RenderLayer, x: Int, y: Int, z: Int, color: LyraColor) {
        val p1 = LyraVector.Companion.of(x, y, z)
        val p2 = p1 + LyraVector.UNIT_CUBE
        renderFilled(layer, p1, p2, color)
    }
}
