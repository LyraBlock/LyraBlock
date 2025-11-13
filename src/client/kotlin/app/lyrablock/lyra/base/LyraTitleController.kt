package app.lyrablock.lyra.base

import app.lyrablock.lyra.LyraModule
import app.lyrablock.lyra.util.LyraIdentifier
import app.lyrablock.lyra.util.MCUtils
import app.lyrablock.lyra.util.render.MatrixStackDSL.translate
import app.lyrablock.lyra.util.render.MatrixStackDSL.uniformScale
import app.lyrablock.orion.render.DrawContextDSL.withPushMatrix
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component

@LyraModule
object LyraTitleController {
    var currentMessage: Component = Component.empty()
    var remainingTicks = 0


    init {
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, LyraIdentifier.of("title"), ::draw)
    }

    @Suppress("UNUSED_PARAMETER")
    fun draw(context: GuiGraphics, tickDeltaManager: DeltaTracker) {
        if (--remainingTicks < 0) return
        // The font size (scaled)
        val size = 3
        val textRenderer = MCUtils.theClient.font
        val height = textRenderer.lineHeight * size
        val width = textRenderer.width(currentMessage) * size
        val x = (context.guiWidth() - width) / 2
        val y = (context.guiHeight() - height) / 2
        context.withPushMatrix {
            pose().translate(x, y).uniformScale(size)
            drawString(textRenderer, currentMessage, 0, -10, 0xFFFFFF)
        }
    }

    /**
     * @param duration The duration in ticks.
     */
    fun show(message: Component, duration: Int = 200) {
        currentMessage = message
        remainingTicks = duration
    }
}
