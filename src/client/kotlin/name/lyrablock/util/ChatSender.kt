package name.lyrablock.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.text.Text

object ChatSender {
    val PREFIX: Text = Text.of("§3Lyra§bBlock §7» §r")

    fun sendInfo(message: Text) {
        val client = MinecraftClient.getInstance()
        val player: ClientPlayerEntity = client.player ?: return
        // Player must be client-side, or we are fucked up.
        player.sendMessage(PREFIX.copy().append(message), false)
    }

    fun sendInfo(message: String) {
        sendInfo(Text.of(message))
    }
}
