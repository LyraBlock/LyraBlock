package app.lyrablock.lyra.util.render

import app.lyrablock.lyra.util.math.LyraVector
import app.lyrablock.orion.math.OrionColor
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.ShapeRenderer

object WorldRenderDSL {
    // The world render context is translated to the player's camera pos.
    // This function translates the matrices back to the origin.
    private inline fun WorldRenderContext.translatedToOrigin(operation: () -> Unit) {
        val matrices = this.matrices() ?: return
        val cameraPos = this.gameRenderer().mainCamera.position

        matrices.pushPose()
        matrices.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z)
        operation.invoke()
        matrices.popPose()
    }

    fun WorldRenderContext.renderLine(layer: RenderType, p1: LyraVector, p2: LyraVector, color: OrionColor) {
        val matrices = this.matrices() ?: return
        val consumers = this.consumers() as MultiBufferSource.BufferSource
        val buffer = consumers.getBuffer(layer)

        translatedToOrigin {
            val matrix = matrices.last()
            val normal = (p1 - p2).normalized().toVector3f()

            buffer.addVertex(matrix, p1.toVector3f()).setColor(color.argb).setNormal(matrix, normal)
            buffer.addVertex(matrix, p2.toVector3f()).setColor(color.argb).setNormal(matrix, normal)

            consumers.endBatch(layer)
        }

    }

    fun WorldRenderContext.renderOutline(
        layer: RenderType, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double, color: OrionColor
    ) {
        val matrices = this.matrices() ?: return

        translatedToOrigin {
            val consumers = this.consumers() as MultiBufferSource.BufferSource
            val buffer = consumers.getBuffer(layer)

            ShapeRenderer.renderLineBox(
                matrices.last(), buffer, x1, y1, z1, x2, y2, z2, color.redF, color.greenF, color.blueF, color.alphaF
            )
            consumers.endBatch(layer)
        }
    }

    fun WorldRenderContext.renderOutline(layer: RenderType, p1: LyraVector, p2: LyraVector, color: OrionColor) =
        renderOutline(layer, p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, color)

    fun WorldRenderContext.renderBlockOutline(layer: RenderType, x: Int, y: Int, z: Int, color: OrionColor) {
        val p1 = LyraVector.Companion.of(x, y, z)
        val p2 = p1 + LyraVector.Companion.UNIT_CUBE
        renderOutline(layer, p1, p2, color)
    }

    // This waypoint is left for mining waypoints, which should be integer.
    fun WorldRenderContext.renderLineFromCrosshair(layer: RenderType, x: Int, y: Int, z: Int, color: OrionColor) {
        // Translate to the center of the block.
        val target = LyraVector.Companion.of(x + .5, y + .5, z + .5)
        val camera = this.gameRenderer().mainCamera
        val cameraPos = camera.position

        // The position 1 block forward from the camera.
        // Since the render layer is above blocks, being translated into blocks does not matter.
        val cameraForward = LyraVector.of(cameraPos) + LyraVector.directionFromPolar(
            camera.xRot, camera.yRot
        )

        renderLine(layer, cameraForward, target, color)
    }

    /**
     * Render a filled box.
     */
    fun WorldRenderContext.renderFilled(
        layer: RenderType,
        x1: Double,
        y1: Double,
        z1: Double,
        x2: Double,
        y2: Double,
        z2: Double,
        color: OrionColor
    ) {
        val matrices = this.matrices() ?: return

        translatedToOrigin {
            val consumers = this.consumers() ?: return
            val buffer = consumers.getBuffer(layer)

            ShapeRenderer.addChainedFilledBoxVertices(matrices, buffer, x1, y1, z1, x2, y2, z2,
                color.redF, color.greenF, color.blueF, color.alphaF
            )
        }
    }

    fun WorldRenderContext.renderFilled(
        layer: RenderType, p1: LyraVector, p2: LyraVector, color: OrionColor
    ) {
        renderFilled(layer, p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, color)
    }

    fun WorldRenderContext.renderBlockFilled(layer: RenderType, x: Int, y: Int, z: Int, color: OrionColor) {
        val p1 = LyraVector.Companion.of(x, y, z)
        val p2 = p1 + LyraVector.UNIT_CUBE
        renderFilled(layer, p1, p2, color)
    }
}
