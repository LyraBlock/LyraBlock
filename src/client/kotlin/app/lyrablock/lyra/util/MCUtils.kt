package app.lyrablock.lyra.util

import net.minecraft.client.MinecraftClient

object MCUtils {
    val theClient get() = MinecraftClient.getInstance()
    val thePlayer get() = theClient.player
}
