package app.lyrablock.lyra.feature.chat

import app.lyrablock.lyra.LyraModule
import app.lyrablock.lyra.util.MCUtils
import app.lyrablock.lyra.util.chat.HypixelChatUtils
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents

@LyraModule
@Suppress("unused")
object CoordinatesSender {
    init {
        ClientSendMessageEvents.MODIFY_CHAT.register(::modifyChat)
        ClientSendMessageEvents.MODIFY_COMMAND.register(HypixelChatUtils.getModifierForCommands(::modifyChat))
    }

    fun getCoordinates(): String {
        val player = MCUtils.thePlayer!!
        val pos = player.pos
        return String.format("%.1f %.1f %.1f", pos.x, pos.y, pos.z)
    }

    fun modifyChat(message: String): String {
        if (message.trim() == "!c") return getCoordinates()
        return message
    }
}
