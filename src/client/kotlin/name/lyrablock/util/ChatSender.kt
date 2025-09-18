package name.lyrablock.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.text.Text
import net.minecraft.text.Text.literal

object ChatSender {
//    val PREFIX: Text = Text.of("§3Lyra§bBlock §7» §r")
    val PREFIX: Text = literal("").apply {
        append(literal("L").withColor(0x66ffff))
        append(literal("y").withColor(0x5ef4f4))
        append(literal("r").withColor(0x57eaea))
        append(literal("a").withColor(0x4fdfdf))
        append(literal("B").withColor(0x2fb6b6))
        append(literal("l").withColor(0x26acac))
        append(literal("o").withColor(0x1ca2a2))
        append(literal("c").withColor(0x119999))
        append(literal("k").withColor(0x008f8f))
        append(literal(" » "))
    }

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
