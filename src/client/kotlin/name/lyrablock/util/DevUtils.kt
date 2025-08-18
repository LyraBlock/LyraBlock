package name.lyrablock.util

import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier

object DevUtils {
    fun drawTestText(context: DrawContext, x: Int, y: Int, text: String) {
        context.drawText(
            MinecraftClient.getInstance().textRenderer,
            text,
            x,
            y,
            0xFFFFFFFF.toInt(),
            true
        )
    }

    fun registerDrawTestText(x: Int, y: Int, text: () -> String) {
        HudLayerRegistrationCallback.EVENT.register {
            it.attachLayerAfter(
                IdentifiedLayer.MISC_OVERLAYS, Identifier.of("lyra:test")
            ) { context, tickDeltaManager ->
                drawTestText(context, x, y, text())
            }
        }
    }
}
