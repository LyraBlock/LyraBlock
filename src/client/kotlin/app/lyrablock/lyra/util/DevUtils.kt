package app.lyrablock.lyra.util


import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.resources.ResourceLocation
import java.util.*

object DevUtils {
    fun drawTestText(context: GuiGraphics, x: Int, y: Int, text: String) {
        context.drawString(
            Minecraft.getInstance().font,
            text,
            x,
            y,
            0xFFFFFFFF.toInt(),
            true
        )
    }

    fun registerDrawTestText(x: Int, y: Int, text: () -> String) {
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, ResourceLocation.parse("lyra:${UUID.randomUUID()}")) { context, _ ->
            drawTestText(context, x, y, text())
        }
    }
}
