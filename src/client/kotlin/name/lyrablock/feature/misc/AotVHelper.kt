package name.lyrablock.feature.misc

import name.lyrablock.util.math.LyraColor
import name.lyrablock.util.render.LyraRenderLayer
import name.lyrablock.util.render.WorldRenderDSL.renderBlockFilled
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.item.Items
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext

object AotVHelper {
    private val OVERLAY_COLOR =
        LyraColor.argb(0xff8133d9).withAlpha(0.3f)

    init {
        WorldRenderEvents.AFTER_ENTITIES.register(::render)
    }

    fun render(context: WorldRenderContext) {
        val player = MinecraftClient.getInstance().player ?: return

        if (!player.isSneaking) return

        val inventory = player.inventory
        val isAotVItem = inventory.selectedStack.item in arrayOf(Items.DIAMOND_SWORD, Items.DIAMOND_SWORD)

        if (!isAotVItem) return

        // TODO: detect if the SkyBlock id is AotE/AotV

        val world = context.world()
        val camera = context.camera()
        val start = camera.pos
        val end = start.add(Vec3d.fromPolar(camera.pitch, camera.yaw).multiply(43.0))

        val hitResult = world.raycast(
            RaycastContext(
                start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, player
            )
        )
        if (hitResult.type != HitResult.Type.BLOCK) return

        val block = hitResult.blockPos
        val blockAbove = world.getBlockState(block.up())

        if (!blockAbove.isAir) return

        context.renderBlockFilled(
            LyraRenderLayer.FILLED,
            block.x,
            block.y,
            block.z,
            OVERLAY_COLOR
        )

//
//        context.renderBlockFilled(LyraRenderLayer.QUADS, targetX, targetY, targetZ, LyraColor.WHITE)
    }
}
