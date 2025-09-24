package app.lyrablock.lyra.feature.chat

import app.lyrablock.lyra.LyraModule
import app.lyrablock.lyra.event.ClickChatEvent
import app.lyrablock.lyra.event.MouseClickData
import app.lyrablock.lyra.mixin.client.ChatHudAccessor
import app.lyrablock.lyra.util.KeyboardUtils
import net.minecraft.client.MinecraftClient

@LyraModule
object ChatCopyHandler {
    init {
        ClickChatEvent.Companion.EVENT.register(::onMessageClick)
    }

    private fun getMessageLineIndexByMouse(accessor: ChatHudAccessor, mouseX: Double, mouseY: Double): Int =
        accessor.invokeGetMessageLineIndex(
            accessor.invokeToChatLineX(mouseX),
            accessor.invokeToChatLineY(mouseY)
        )

    fun onMessageClick(data: MouseClickData, chatHud: ChatHudAccessor) {
        val (mouseX, mouseY, button) = data
        val client = MinecraftClient.getInstance()
        if (button != 0 || !KeyboardUtils.isControlDown()) return

        val lineIndex = getMessageLineIndexByMouse(chatHud, mouseX, mouseY)
        if (lineIndex !in 0 until chatHud.getVisibleMessages().size) return

        val message = chatHud.getVisibleMessages()[lineIndex]

        client.keyboard.clipboard = buildString {
            message.content().accept { _, _, codePoint ->
                appendCodePoint(codePoint)
                true
            }
        }
    }
}
