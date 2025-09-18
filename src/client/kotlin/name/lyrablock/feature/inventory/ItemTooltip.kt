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

/**
 * Handles the display of item value tooltips in the inventory UI.
 * Optimized for SkyBlock items, with scrollable overflow and extra info.
 */
@Suppress("unused")
@LyraModule
object ItemTooltip {
    private const val PADDING = 2
    private val positioner = HoveredTooltipPositioner.INSTANCE!!

    // Used to reset scroll offset when a new item is hovered
    private var current by observable(0) { _, _, _ -> offset = 0.0 }
    private var offset = 0.0
    private var maxOffset = 0

    private val enableItemValue get() = true //TODO config in 3-5 business days

    init {
        HandledScreenEvents.MODIFY_ITEM_TOOLTIP.register(::onDrawItemTooltip)
        HandledScreenEvents.MOUSE_SCROLLED.register(::onMouseScrolled)
    }

    /**
     * Handles mouse scroll events to scroll the tooltip if overflowing.
     */
    @Suppress("UNUSED_PARAMETER")
    fun onMouseScrolled(mouseX: Double, mouseY: Double, hAmount: Double, vAmount: Double) {
        offset = (offset - vAmount * 5).coerceIn(0.0, maxOffset.toDouble())
    }

    /**
     * Draws the custom tooltip for SkyBlock items, with extra info and scroll support.
     */
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
        // Only handle SkyBlock items for performance
        val id = stack.getSkyBlockID() ?: return CancellableEventResult.PASS
        val hasUuid = stack.getSkyBlockUUID() != null
        val onBazaar = BazaarTracker.isProduct(id)
        val onAuction = !onBazaar
        check(onBazaar xor onAuction) // Should never both be true or false

        val fontHeight = textRenderer.fontHeight
        val tooltipWidth = text.maxOf { textRenderer.getWidth(it) }
        val tooltipHeight = (if (text.size == 1) -2 else 0) + fontHeight * text.size
        val extraLines = (onBazaar * 2) + (onAuction * 3) + (hasUuid * 1)
        val extraHeight = if (enableItemValue) {extraLines * fontHeight + PADDING} else 0
        val scaledWidth = context.scaledWindowWidth
        val scaledHeight = context.scaledWindowHeight
        val height = tooltipHeight + extraHeight
        val (positionerX, positionerY) = positioner.getPosition(scaledWidth, scaledHeight, x, y, tooltipWidth, height)
        val overflow = tooltipHeight + extraHeight > scaledHeight
        val actualHeight = if (overflow) scaledHeight - extraHeight - 4 else height
        val actualY = if (overflow) 4 else positionerY
        maxOffset = (tooltipHeight - actualHeight).coerceAtLeast(0)

        val client = MCUtils.theClient
        val scale = client.window.scaleFactor
        val fbHeight = client.window.framebufferHeight

        // Draw tooltip background
        TooltipBackgroundRenderer.render(
            context,
            positionerX,
            actualY,
            tooltipWidth + overflow * 1,
            actualHeight,
            400, texture
        )

        // Draw tooltip content, with scissor for overflow
        context.withPushMatrix {
            matrices.translate(0, if (overflow) -offset else 0.0)
            if (overflow) {
                context.enableScissor(
                    positionerX, actualY + offset.toInt(),
                    positionerX + tooltipWidth, actualY + actualHeight + offset.toInt()
                )
            }
            drawNormalTooltip(context, textRenderer, text, positionerX, actualY)
            if (overflow) context.disableScissor()
        }

        context.takeIf { overflow }?.withPushMatrix {
            matrices.translate(0, 0, 400)
            // Draw scrollbar
            val barHeight = (actualHeight.toFloat() / tooltipHeight * actualHeight).toInt().coerceAtLeast(10)
            val barY = (offset / maxOffset * (actualHeight - barHeight)).toInt() + actualY
            val barX = positionerX + tooltipWidth + 1
            context.fill(barX, actualY, barX + 1, actualY + actualHeight, -0xaaaaab)
            context.fill(barX, barY, barX + 1, barY + barHeight, -0x555556)
        }

        return CancellableEventResult.CANCEL
    }

    /**
     * Draws the main tooltip text lines at the given position.
     */
    private fun drawNormalTooltip(
        context: DrawContext,
        textRenderer: TextRenderer,
        text: List<Text>,
        x: Int,
        y: Int,
    ) {
        context.withPushMatrix {
            matrices.translate(x, y, 400)
            text.forEachIndexed { index, line ->
                drawText(textRenderer, line, 0, 0, 0xffffff, false)
                matrices.translate(0, textRenderer.fontHeight + if (index == 0) 2 else 0)
            }
        }
    }
}
