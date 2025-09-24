package app.lyrablock.lyra.util


import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.util.Identifier
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

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

    @OptIn(ExperimentalUuidApi::class)
    fun registerDrawTestText(x: Int, y: Int, text: () -> String) {
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, Identifier.of("lyra:${Uuid.random()}")) { context, tickDeltaManager ->
            drawTestText(context, x, y, text())
        }
    }
}
