package name.lyrablock.util.waypoint

import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.util.Identifier


object WaypointController {
    val IDENTIFIER: Identifier = Identifier.of("lyra", "waypoint")

    init {
        HudLayerRegistrationCallback.EVENT.register {
            it.attachLayerAfter(IdentifiedLayer.MISC_OVERLAYS, IDENTIFIER, ::render)
        }

//        WorldRenderEvents.AFTER_ENTITIES.register { context ->
//            val client = MinecraftClient.getInstance()
//            val world = client.world ?: return@register
//            val tesselator = Tessellator.getInstance()
//            val bufferBuilder = tesselator.begin(
//                VertexFormat.DrawMode.LINES,
//                VertexFormats.LINES
//            )
//            val matrices = context.matrixStack() ?: return@register
//            val camera = context.camera().pos
//            val cameraX = camera.x
//            val cameraY = camera.y
//            val cameraZ = camera.z
//            matrices.push()
//            matrices.translate(-cameraX, -cameraY, -cameraZ)
//            val positionMatrix = context.positionMatrix()
//
//            bufferBuilder.vertex(positionMatrix, 0f, -60f ,0f)
//            bufferBuilder.vertex(positionMatrix, 1f, -60f ,1f)
//            val builtBuffer = bufferBuilder.end()
//
//            BufferRenderer.drawWithGlobalProgram(builtBuffer)
//        }
    }

    fun render(context: DrawContext, tickDeltaManager: RenderTickCounter) {
//        context.drawLine(10f, 10f, 20f, 20f)
    }
}
