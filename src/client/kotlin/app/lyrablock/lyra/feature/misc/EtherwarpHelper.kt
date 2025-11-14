package app.lyrablock.lyra.feature.misc

import app.lyrablock.lyra.LyraModule
import app.lyrablock.lyra.event.FovEvents
import app.lyrablock.lyra.util.MCUtils
import app.lyrablock.lyra.util.item.ItemUtils.getCustomData
import app.lyrablock.lyra.util.position
import app.lyrablock.lyra.util.render.LyraRenderLayer
import app.lyrablock.lyra.util.render.WorldRenderDSL.renderBlockFilled
import app.lyrablock.orion.math.OrionColor
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.BlockPos
import net.minecraft.tags.BlockTags
import net.minecraft.util.Mth
import net.minecraft.world.item.Items
import net.minecraft.world.level.ClipContext
import net.minecraft.world.phys.HitResult
import net.minecraft.world.phys.Vec3

@LyraModule
object EtherwarpHelper {
    private val OVERLAY_COLOR = OrionColor.rgb(0x8133d9).withAlpha(0.3f)

    private var fovMultiplier: Float = 1f
    private var oldFovMultiplier: Float = 1f

    init {
        WorldRenderEvents.END_MAIN.register(::render)
        FovEvents.MULTIPLY.register(::multiplyFov)
        ClientTickEvents.START_CLIENT_TICK.register { warpTarget = getEtherwarpTarget() }
        ClientTickEvents.END_CLIENT_TICK.register { oldFovMultiplier = fovMultiplier }
    }

    private val ETHERWARP_ITEMS = setOf(
        Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL
    )

    private var warpTarget: BlockPos? = null

    /**
     * Check if the player is ready to etherwarp.
     * @return The etherwarp distance; -1 if not available.
     */
    private fun getEtherwarpDistance(player: LocalPlayer): Int {
        val inventory = player.inventory
        val selectedStack = inventory.selectedItem
        val isHolding = selectedStack.item in ETHERWARP_ITEMS
        val customData = selectedStack.getCustomData() ?: return -1
        if (!isHolding) return -1
        if (!player.isShiftKeyDown) return -1
        if (customData.getByteOr("ethermerge", 0.toByte()) != 1.toByte()) return -1

        val tunedTransmission = customData.getIntOr("tuned_transmission", 0)
        val maxDistance = 57 + tunedTransmission

        return maxDistance
    }

    private fun getEtherwarpTarget(): BlockPos? {
        val player = MCUtils.thePlayer ?: return null
        val distance = getEtherwarpDistance(player)
        if (distance < 0) return null

        val gameRenderer = MCUtils.theClient.gameRenderer
        val camera = gameRenderer.mainCamera
        val world = MCUtils.theClient.level!!
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

    private fun render(context: WorldRenderContext) {
        val block = warpTarget ?: return

        context.renderBlockFilled(
            LyraRenderLayer.FILLED, block.x, block.y, block.z, OVERLAY_COLOR
        )
    }

    private fun multiplyFov(tickDelta: Float): Float {
        val target = warpTarget ?: return 1f
        val player = MCUtils.thePlayer?.position ?: return 1f
        val distance = target.center.distanceTo(player)
        fovMultiplier = (2f / distance).toFloat().coerceAtMost(1f)
        return Mth.lerp(tickDelta, oldFovMultiplier, fovMultiplier)
    }
}
