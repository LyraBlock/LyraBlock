package name.lyrablock.feature.inventory

import name.lyrablock.LyraModule
import name.lyrablock.event.CancellableEventResult
import name.lyrablock.event.HandledScreenEvents
import name.lyrablock.util.AbuseBoolean.times
import name.lyrablock.util.MCUtils
import name.lyrablock.util.item.ItemUtils.getSkyBlockID
import name.lyrablock.util.item.ItemUtils.getSkyBlockUUID
import name.lyrablock.util.render.DrawContextDSL.withPushMatrix
import name.lyrablock.util.render.MatrixStackDSL.translate
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer
import net.minecraft.item.tooltip.TooltipData
import net.minecraft.screen.slot.Slot
import net.minecraft.text.Text
import net.minecraft.util.Identifier
import org.joml.component1
import org.joml.component2
import kotlin.properties.Delegates.observable
import kotlin.uuid.ExperimentalUuidApi

@LyraModule
object ItemValueDisplay {
    const val PADDING = 2
    val positioner = HoveredTooltipPositioner.INSTANCE!!

    var current by observable(0) { _, _, _ ->
        offset = 0.0
    }
    var offset = 0.0
    var maxOffset = 0


    init {
        HandledScreenEvents.MODIFY_ITEM_TOOLTIP.register(::onDrawItemTooltip)
        HandledScreenEvents.MOUSE_SCROLLED.register(::onMouseScrolled)
    }

    @Suppress("UNUSED_PARAMETER")
    fun onMouseScrolled(mouseX: Double, mouseY: Double, hAmount: Double, vAmount: Double) {
        offset = (offset - vAmount * 5).coerceIn(0.0, maxOffset.toDouble())
    }

    @OptIn(ExperimentalUuidApi::class)
    @Suppress("UNUSED_PARAMETER")
    fun onDrawItemTooltip(
        context: DrawContext,
        focusedSlot: Slot,
        textRenderer: TextRenderer,
        text: List<Text>,
        data: TooltipData?,
        x: Int,
        y: Int,
        texture: Identifier?
    ): CancellableEventResult {
        val stack = focusedSlot.stack
//        current = stack.hashCode()
        // To save resources, disables all tooltip modifications for non-SkyBlock items.
        // This made the performance better, in exchange for its generality.
        val id = stack.getSkyBlockID() ?: return CancellableEventResult.PASS

        val hasUuid: Boolean = stack.getSkyBlockUUID() != null

        val onBazaar = BazaarTracker.isProduct(id)
        val onAuction = !onBazaar
        assert(onBazaar xor onAuction)

        val fontHeight = textRenderer.fontHeight

        // Also is what Mojang wrote. I just translated them into Kotlin.
        val tooltipWidth = text.maxOf { textRenderer.getWidth(it) }
        val tooltipHeight = (if (text.size == 1) (-2) else 0) + fontHeight * text.size

        val width = tooltipWidth

        val extraLines = onBazaar * 2 + onAuction * 3 + hasUuid * 1
        val extraHeight = extraLines * fontHeight

        val scaledWidth = context.scaledWindowWidth
        val scaledHeight = context.scaledWindowHeight

        val height = tooltipHeight + extraHeight + PADDING

        val (positionerX, positionerY) = positioner.getPosition(scaledWidth, scaledHeight, x, y, tooltipWidth, height)

        val overflow = tooltipHeight + PADDING + extraHeight > scaledHeight

        val actualHeight = if (overflow) scaledHeight - PADDING - extraHeight - 4 else height
        val actualY = if (overflow) 4 else positionerY

        maxOffset = (tooltipHeight - actualHeight).coerceAtLeast(0)


        val client = MCUtils.theClient
        val scale = client.window.scaleFactor
        val fbHeight = client.window.framebufferHeight

        TooltipBackgroundRenderer.render(
            context,
            positionerX,
            actualY,
            tooltipWidth,
            actualHeight,
            400, texture)

        context.withPushMatrix {
            matrices.translate(0, overflow * (-offset))

            if (overflow) {
                context.enableScissor(
                    positionerX, actualY+offset.toInt(),
                    positionerX + tooltipWidth, actualY + actualHeight + offset.toInt()
                )
            }

            drawNormalTooltip(context, textRenderer, text, positionerX, actualY)

            if (overflow) context.disableScissor()

        }


        return CancellableEventResult.CANCEL
    }

    fun drawNormalTooltip(
        context: DrawContext,
        textRenderer: TextRenderer,
        text: List<Text>,
        x: Int,
        y: Int,
    ) {

        context.withPushMatrix {
            matrices.translate(x, y, 400)

            text.forEachIndexed { index, it ->
                drawText(textRenderer, it, 0, 0, 0xffffff, false)
                matrices.translate(0, textRenderer.fontHeight + (if (index == 0) 2 else 0))
            }
        }
    }
}
