package name.lyrablock.util

import net.minecraft.client.MinecraftClient

object MCUtils {
    val theClient get() = MinecraftClient.getInstance()
    val thePlayer get() = theClient.player
}
