package app.lyrablock.lyra.util

import net.minecraft.client.Minecraft

object MCUtils {
    val theClient get() = Minecraft.getInstance()!!
    val thePlayer get() = theClient.player
}
