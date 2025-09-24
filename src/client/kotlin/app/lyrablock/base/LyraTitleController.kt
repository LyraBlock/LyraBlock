package app.lyrablock.base

import app.lyrablock.LyraModule
import app.lyrablock.util.LyraIdentifier
import app.lyrablock.util.MCUtils
import app.lyrablock.util.render.DrawContextDSL.withPushMatrix
import app.lyrablock.util.render.MatrixStackDSL.translate
import app.lyrablock.util.render.MatrixStackDSL.uniformScale
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.text.Text

@LyraModule
object LyraTitleController {
    var currentMessage: Text = Text.empty()
    var remainingTicks = 0


    init {
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, LyraIdentifier.of("title"), ::draw)
    }

    @Suppress("UNUSED_PARAMETER")
    fun draw(context: DrawContext, tickDeltaManager: RenderTickCounter) {
        if (--remainingTicks < 0) return
        // The font size (scaled)
        val size = 3
        val textRenderer = MCUtils.theClient.textRenderer
        val height = textRenderer.fontHeight * size
        val width = textRenderer.getWidth(currentMessage) * size
        val x = (context.scaledWindowWidth - width) / 2
        val y = (context.scaledWindowHeight - height) / 2
        context.withPushMatrix {
            matrices.translate(x, y).uniformScale(size)
            drawTextWithShadow(textRenderer, currentMessage, 0, -10, 0xFFFFFF)
        }
    }

    /**
     * @param duration The duration in ticks.
     */
    fun show(message: Text, duration: Int = 200) {
        currentMessage = message
        remainingTicks = duration
    }
}
