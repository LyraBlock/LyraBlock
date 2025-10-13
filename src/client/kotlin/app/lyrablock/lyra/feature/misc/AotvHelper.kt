package app.lyrablock.lyra.feature.misc

import app.lyrablock.lyra.LyraModule
import app.lyrablock.lyra.util.item.ItemUtils.getCustomData
import app.lyrablock.lyra.util.render.LyraRenderLayer
import app.lyrablock.lyra.util.render.WorldRenderDSL.renderBlockFilled
import app.lyrablock.orion.math.OrionColor
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.item.Items
import net.minecraft.registry.tag.BlockTags
import net.minecraft.util.hit.HitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.world.RaycastContext

@LyraModule
object AotvHelper {
    private val OVERLAY_COLOR = OrionColor.argb(0xff8133d9).withAlpha(0.3f)

    init {
        WorldRenderEvents.AFTER_TRANSLUCENT.register(::render)
    }

    val AOTV_ITEMS = setOf(
        Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL
    )


    /**
     * Check if the player is ready to etherwarp.
     * @return The etherwarp distance; -1 if not available.
     */
    fun getEtherwarpDistance(player: ClientPlayerEntity): Int {
        val inventory = player.inventory
        val selectedStack = inventory.selectedStack
        val isHoldingAotv = selectedStack.item in AOTV_ITEMS
        val customData = selectedStack.getCustomData() ?: return -1
        if (!isHoldingAotv) return -1
        if (!player.isSneaking) return -1
        if (customData.getByte("ethermerge", 0.toByte()) != 1.toByte()) return -1

        val tunedTransmission = customData.getInt("tuned_transmission", 0)
        val maxDistance = 57 + tunedTransmission

        return maxDistance
    }

    fun getEtherwarpTarget(context: WorldRenderContext, player: ClientPlayerEntity): BlockPos? {
        val distance = getEtherwarpDistance(player)
        if (distance < 0) return null

        val world = context.world()
        val camera = context.camera()
        val start = camera.pos
        val end = start.add(Vec3d.fromPolar(camera.pitch, camera.yaw).multiply(distance.toDouble()))

        val hitResult = world.raycast(
            RaycastContext(
                start, end, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.ANY, player
            )
        )
        if (hitResult.type != HitResult.Type.BLOCK) return null

        val block = hitResult.blockPos
        val blockAbove = world.getBlockState(block.up())

        // TODO: confirm if the tag is correct.
        if (!blockAbove.isIn(BlockTags.REPLACEABLE)) return null

        return block
    }

    fun render(context: WorldRenderContext) {
        val player = MinecraftClient.getInstance().player ?: return

        val block = getEtherwarpTarget(context, player) ?: return


        context.renderBlockFilled(
            LyraRenderLayer.FILLED, block.x, block.y, block.z, OVERLAY_COLOR
        )

        // TODO: FOV change so one can see the targeted block clearly
    }
}
