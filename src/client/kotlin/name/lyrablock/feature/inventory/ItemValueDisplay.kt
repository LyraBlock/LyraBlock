package name.lyrablock.feature.inventory

import name.lyrablock.LyraModule
import name.lyrablock.event.CancellableEventResult
import name.lyrablock.event.DrawEvents
import name.lyrablock.util.MCUtils
import name.lyrablock.util.item.ItemUtils.getSkyBlockID
import name.lyrablock.util.render.DrawContextDSL.withPushMatrix
import name.lyrablock.util.render.MatrixStackDSL.translate
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack
import org.joml.component1
import org.joml.component2
import kotlin.uuid.ExperimentalUuidApi

@LyraModule
object ItemValueDisplay {
    const val PADDING = 2
    val positioner = HoveredTooltipPositioner.INSTANCE

    init {
        DrawEvents.MODIFY_ITEM_TOOLTIP.register(::onDrawItemTooltip)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Suppress("UNUSED_PARAMETER")
    fun onDrawItemTooltip(
        context: DrawContext,
        textRenderer: TextRenderer,
        stack: ItemStack,
        x: Int,
        y: Int
    ): CancellableEventResult {
        // To save resources, disables all tooltip modifications for non-SkyBlock items.
        // This made the performance better, in exchange for its generality.
        if (stack.getSkyBlockID() != null) {
            return CancellableEventResult.PASS
        }

        // Map tooltips to components - what Mojang did.

        val tooltip = Screen.getTooltipFromItem(MCUtils.theClient, stack)

        // Also is what Mojang wrote. I just translated them into Kotlin.
        val tooltipWidth = tooltip.maxOf { textRenderer.getWidth(it) }
        val tooltipHeight = (if (tooltip.size == 1) (-2) else 0) + tooltip.sumOf { textRenderer.fontHeight }

        val extraHeight = 1

        val scaledWidth = context.scaledWindowWidth
        val scaledHeight = context.scaledWindowHeight

        val height = tooltipHeight + extraHeight + PADDING

        if (tooltipHeight + PADDING + extraHeight < scaledHeight) {
            val (u, v) = positioner.getPosition(
                scaledWidth,
                scaledHeight,
                x,
                y,
                tooltipWidth,
                height
            )

            TooltipBackgroundRenderer.render(
                context,
                u,
                v,
                tooltipWidth,
                height,
                400,
                stack.get(DataComponentTypes.TOOLTIP_STYLE)
            )

            context.withPushMatrix {
                tooltip.forEach {
                    drawText(textRenderer, it, 0, 0, 0xffffff, false)
                    matrices.translate(0, textRenderer.fontHeight)
                }
            }
        }


        return CancellableEventResult.CANCEL
    }
}
