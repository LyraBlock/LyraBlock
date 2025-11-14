package app.lyrablock.lyra.feature.misc

import app.lyrablock.lyra.LyraModule
import app.lyrablock.lyra.util.MCUtils
import app.lyrablock.lyra.util.item.ItemUtils.getCustomData
import app.lyrablock.lyra.util.render.LyraRenderLayer
import app.lyrablock.lyra.util.render.WorldRenderDSL.renderBlockFilled
import app.lyrablock.orion.math.OrionColor
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.world.item.Items
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

@LyraModule
object AotvHelper {
    private val OVERLAY_COLOR = OrionColor.argb(0xff8133d9).withAlpha(0.3f)

    init {
        WorldRenderEvents.END_MAIN.register(::render)
    }

    val AOTV_ITEMS = setOf(
        Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL
    )


    /**
     * Check if the player is ready to etherwarp.
     * @return The etherwarp distance; -1 if not available.
     */
    fun getEtherwarpDistance(player: LocalPlayer): Int {
        val inventory = player.inventory
        val selectedStack = inventory.selectedItem
        val isHoldingAotv = selectedStack.item in AOTV_ITEMS
        val customData = selectedStack.getCustomData() ?: return -1
        if (!isHoldingAotv) return -1
        if (!player.isShiftKeyDown) return -1
        if (customData.getByteOr("ethermerge", 0.toByte()) != 1.toByte()) return -1

        val tunedTransmission = customData.getIntOr("tuned_transmission", 0)
        val maxDistance = 57 + tunedTransmission

        return maxDistance
    }

    fun getEtherwarpTarget(context: WorldRenderContext, player: LocalPlayer): BlockPos? {
        val distance = getEtherwarpDistance(player)
        if (distance < 0) return null

        val gameRenderer = context.gameRenderer()
        val camera = gameRenderer.mainCamera
        val world = MCUtils.theClient.level!!
        val player = MCUtils.thePlayer!!
        val (pitch, yaw) = camera.xRot to camera.yRot
        val start = camera.position
        val end = start.add(Vec3.directionFromRotation(pitch, yaw).scale(distance.toDouble()))

        val hitResult = world.clip(
            ClipContext(
                start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.ANY, player
            )
        )
        if (hitResult.type != HitResult.Type.BLOCK) return null

        val block = hitResult.blockPos
        val blockAbove = world.getBlockState(block.above())

        // TODO: confirm if the tag is correct.
        if (!blockAbove.`is`(BlockTags.REPLACEABLE)) return null

        return block
    }

    fun render(context: WorldRenderContext) {
        val player = Minecraft.getInstance().player ?: return

        val block = getEtherwarpTarget(context, player) ?: return


        context.renderBlockFilled(
            LyraRenderLayer.FILLED, block.x, block.y, block.z, OVERLAY_COLOR
        )

    }
}
