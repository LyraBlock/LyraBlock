package name.lyrablock.feature.items

import name.lyrablock.LyraModule
import name.lyrablock.event.CancellableEventResult
import name.lyrablock.event.HandledScreenEvents
import name.lyrablock.util.AbuseBoolean.times
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
import net.minecraft.text.Text.literal
import net.minecraft.util.Formatting
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
    private const val PADDING = 9
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
        val stack = focusedSlot.stack
        val id = stack.getSkyBlockID() ?: return CancellableEventResult.PASS
        val hasUuid = stack.getSkyBlockUUID() != null
        val onBazaar = BazaarTracker.isProduct(id)
        val onAuction = !onBazaar

        val tooltipDimensions = calculateTooltipDimensions(textRenderer, text, onBazaar, onAuction, hasUuid)
        val (tooltipWidth, tooltipHeight, extraHeight) = tooltipDimensions
        val scaledWidth = context.scaledWindowWidth
        val scaledHeight = context.scaledWindowHeight
        val height = tooltipHeight + extraHeight
        val (positionerX, positionerY) = positioner.getPosition(scaledWidth, scaledHeight, x, y, tooltipWidth, height)
        val overflow = tooltipHeight + extraHeight > scaledHeight
        val actualHeight = if (overflow) scaledHeight - extraHeight - 4 else height
        val actualY = if (overflow) 4 else positionerY
        maxOffset = (tooltipHeight - actualHeight).coerceAtLeast(0)

        drawTooltipBackground(context, positionerX, actualY, tooltipWidth, actualHeight, overflow, texture)
        drawTooltipContent(context, textRenderer, text, positionerX, actualY, tooltipWidth, actualHeight, overflow)
        drawScrollbar(context, positionerX, actualY, tooltipWidth, actualHeight, tooltipHeight, overflow)


        val y = actualY + actualHeight + PADDING - 3
        drawTooltipBackground(context, positionerX, y, 50, extraHeight, false, texture)

        context.withPushMatrix {
            matrices.translate(positionerX, y, 400)
            if (onBazaar) {
                context.drawText(textRenderer,
                    literal("BZ Buy").formatted(Formatting.YELLOW)
                        .append(literal("${BazaarTracker.getStatus(id)?.buyPrice}")),
                    0, 0, 0xffffff, true)
            }
            if (onAuction) {
                // TODO
            }
        }
        return CancellableEventResult.CANCEL
    }


    /**
     * Calculates the dimensions of the tooltip, including extra height for additional lines.
     */
    private fun calculateTooltipDimensions(
        textRenderer: TextRenderer,
        text: List<Text>,
        onBazaar: Boolean,
        onAuction: Boolean,
        hasUuid: Boolean
    ): Triple<Int, Int, Int> {
        val fontHeight = textRenderer.fontHeight
        val tooltipWidth = text.maxOf { textRenderer.getWidth(it) }
        val tooltipHeight = (if (text.size == 1) -2 else 0) + fontHeight * text.size
        val extraLines = (onBazaar * 2) + (onAuction * 3) + (if (hasUuid) 1 else 0)
        val extraHeight = if (enableItemValue && (onBazaar || onAuction)) extraLines * fontHeight + PADDING else 0
        return Triple(tooltipWidth, tooltipHeight, extraHeight)
    }

    /**
     * Draws the tooltip background.
     */
    private fun drawTooltipBackground(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        overflow: Boolean,
        texture: Identifier?
    ) {
        TooltipBackgroundRenderer.render(
            context,
            x,
            y,
            width + if (overflow) 1 else 0,
            height,
            400,
            texture
        )
    }

    /**
     * Draws the tooltip content, with scissor for overflow.
     */
    private fun drawTooltipContent(
        context: DrawContext,
        textRenderer: TextRenderer,
        text: List<Text>,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        overflow: Boolean
    ) {
        context.withPushMatrix {
            matrices.translate(0, if (overflow) -offset else 0.0)
            if (overflow) {
                context.enableScissor(x, y + offset.toInt(), x + width, y + height + offset.toInt())
            }
            drawNormalTooltip(context, textRenderer, text, x, y)
            if (overflow) context.disableScissor()
        }
    }

    /**
     * Draws the scrollbar if the tooltip overflows.
     */
    private fun drawScrollbar(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        height: Int,
        tooltipHeight: Int,
        overflow: Boolean
    ) {
        if (!overflow) return

        context.withPushMatrix {
            matrices.translate(0, 0, 400)
            val barHeight = (height.toFloat() / tooltipHeight * height).toInt().coerceAtLeast(10)
            val barY = (offset / maxOffset * (height - barHeight)).toInt() + y
            val barX = x + width + 1
            context.fill(barX, y, barX + 1, y + height, -0xaaaaab)
            context.fill(barX, barY, barX + 1, barY + barHeight, -0x555556)
        }
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
