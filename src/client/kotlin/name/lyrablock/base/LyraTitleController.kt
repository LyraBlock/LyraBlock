package name.lyrablock.base

import name.lyrablock.LyraModule
import name.lyrablock.util.LyraIdentifier
import name.lyrablock.util.MCUtils
import name.lyrablock.util.render.RenderContextDSL.withPush
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.text.Text

@LyraModule
object LyraTitleController {
    var currentMessage: Text = Text.empty()
    var remainingTicks = 0


    init {
        HudLayerRegistrationCallback.EVENT.register {
            it.attachLayerAfter(
                IdentifiedLayer.MISC_OVERLAYS, LyraIdentifier.of("title"), ::draw
            )
        }
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
        context.matrices.withPush {
            translate(x.toFloat(), y.toFloat(), 0f)
            scale(size.toFloat(), size.toFloat(), 1f)
            context.drawTextWithShadow(textRenderer, currentMessage, 0, -10, 0xFFFFFF)
        }
    }

    /**
     * @param duration The duration in ticks.
     */
    fun show(message: Text, duration: Int = 100) {
        currentMessage = message
        remainingTicks = duration
    }
}
