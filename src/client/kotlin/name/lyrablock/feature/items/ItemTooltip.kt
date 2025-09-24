package app.lyrablock.feature.items

import app.lyrablock.LyraModule
import app.lyrablock.event.CancellableEventResult
import app.lyrablock.event.HandledScreenEvents
import app.lyrablock.util.item.ItemUtils.getSkyBlockID
import app.lyrablock.util.item.ItemUtils.getSkyBlockUUID
import app.lyrablock.util.math.IntRectangle
import app.lyrablock.util.render.DrawContextDSL.withPushMatrix
import app.lyrablock.util.render.MatrixStackDSL.translate
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
 *
 * TODO: this is totally fucked up by 1.21.8
 */
@Suppress("unused")
@LyraModule
object ItemTooltip {
    private const val GAP = 9
    private const val TOOLTIP_Z = 400
    private val positioner = HoveredTooltipPositioner.INSTANCE!!

    // Used to reset scroll offset when a new item is hovered
    private var current by observable(0) { _, _, _ -> offset = 0.0 }
    private var offset = 0.0
    private var maxOffset = 0

    // Configuration placeholder for enabling item value display
    private val enableItemValue: Boolean
        get() = true // TODO: Replace with actual configuration logic

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
        // Basic information
        val stack = focusedSlot.stack
        val id = stack.getSkyBlockID() ?: return CancellableEventResult.PASS
        val hasUuid = stack.getSkyBlockUUID() != null
        val onBazaar = BazaarTracker.isProduct(id)
        val onAuction = false

        // Get extra text (price, museum, etc.)
        val extraText = ItemExtraInformation.of(id, stack.count, hasUuid, onBazaar, onAuction, false)

        // Calculate the dimensions for the tooltip
        val (tooltipContentHeight, extraHeight) = calculateHeight(textRenderer, text, extraText)
        val tooltipWidth = textRenderer.textWidthOf(text)
        val extraWidth = textRenderer.textWidthOf(extraText)
        val totalWidth = maxOf(tooltipWidth, extraWidth)

        val scaledWidth = context.scaledWindowWidth
        val scaledHeight = context.scaledWindowHeight
        val height = tooltipContentHeight + extraHeight + GAP

        // Use the native positioner to calculate the tooltip position.
        // We take the x. The y is discarded if the tooltip overflows.
        val (x, originalY) = positioner.getPosition(scaledWidth, scaledHeight, x, y, totalWidth, height)
        val overflow = height > scaledHeight
        val actualHeight = if (overflow) scaledHeight - extraHeight - 4 else tooltipContentHeight
        // Here 4 is set for the tooltip texture's inset.
        val actualY = if (overflow) 4 else originalY
        maxOffset = (tooltipContentHeight - actualHeight + 3).coerceAtLeast(0)

        val tooltipArea = IntRectangle.sized(x, actualY, tooltipWidth, actualHeight)
        drawBackground(context, tooltipArea.expand(right = if (overflow) 1 else 0), texture)
        drawTooltipContent(context, textRenderer, text, tooltipArea, overflow)
        drawScrollbar(context, tooltipArea, tooltipContentHeight, overflow)

        val yExtra = actualY + actualHeight + GAP
        extraText?.let {
            drawBackground(context, IntRectangle.sized(x, yExtra, extraWidth, extraHeight), null)
            drawTextLines(context, textRenderer, it, x, yExtra, 0)
        }
        return CancellableEventResult.CANCEL
    }

    private fun TextRenderer.textWidthOf(text: List<Text>?) =
        text?.maxOfOrNull { getWidth(it) } ?: 0

    /**
     * Calculates the dimensions of the tooltip, including extra height for additional lines.
     * TODO: cleanup code
     */
    private fun calculateHeight(
        textRenderer: TextRenderer,
        text: List<Text>,
        extraLines: List<Text>?
    ): Pair<Int, Int> {
        val fontHeight = textRenderer.fontHeight
        val tooltipHeight = (if (text.size == 1) -2 else 0) + fontHeight * text.size
        val extraHeight = (extraLines?.size ?: 0) * fontHeight
        return tooltipHeight to extraHeight
    }

    /**
     * Draws the tooltip background.
     */
    private fun drawBackground(
        context: DrawContext,
        area: IntRectangle,
        texture: Identifier?
    ) {
        TooltipBackgroundRenderer.render(
            context, area.x1, area.y1, area.width, area.height, texture
        )
    }

    /**
     * Draws the tooltip content, with scissor for overflow.
     */
    private fun drawTooltipContent(
        context: DrawContext,
        textRenderer: TextRenderer,
        text: List<Text>,
        area: IntRectangle,
        overflow: Boolean
    ) {
        val (x, y) = area.x1 to area.y1
        val (width, height) = area.width to area.height
        context.withPushMatrix {
            matrices.translate(0, if (overflow) -offset else 0.0)
            if (overflow) context.enableScissor(x, y + offset.toInt(), x + width, y + height + offset.toInt())
            drawTextLines(context, textRenderer, text, x, y)
            if (overflow) context.disableScissor()
        }
    }

    /**
     * Draws the scrollbar if the tooltip overflows.
     */
    private fun drawScrollbar(
        context: DrawContext,
        tooltipArea: IntRectangle,
        totalHeight: Int,
        overflow: Boolean
    ) {
        val (x, y) = tooltipArea.x1 to tooltipArea.y1
        val (width, height) = tooltipArea.width to tooltipArea.height
        if (!overflow) return
        context.withPushMatrix {
            matrices.translate(0, 0, TOOLTIP_Z)
            val barHeight = (height.toFloat() / totalHeight * height).toInt().coerceAtLeast(10)
            val barY = (offset / maxOffset * (height - barHeight)).toInt() + y
            val barX = x + width + 1
            context.fill(barX, y, barX + 1, y + height, -0xaaaaab)
            context.fill(barX, barY, barX + 1, barY + barHeight, -0x555556)
        }
    }

    /**
     * Simply draw the text line by line.
     */
    private fun drawTextLines(
        context: DrawContext,
        textRenderer: TextRenderer,
        text: List<Text>,
        x: Int,
        y: Int,
        gapAfterFirst: Int = 0
    ) {
        context.withPushMatrix {
            matrices.translate(x, y, TOOLTIP_Z)
            text.forEachIndexed { index, line ->
                drawText(textRenderer, line, 0, 0, 0xffffff, false)
                matrices.translate(0, textRenderer.fontHeight + if (index == 0) gapAfterFirst else 0)
            }
        }
    }
}
