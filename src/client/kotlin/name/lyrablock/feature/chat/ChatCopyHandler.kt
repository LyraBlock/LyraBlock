package app.lyrablock.feature.chat

import app.lyrablock.LyraModule
import app.lyrablock.event.ClickChatEvent
import app.lyrablock.event.MouseClickData
import app.lyrablock.mixin.client.ChatHudAccessor
import app.lyrablock.util.KeyboardUtils.isControlDown
import net.minecraft.client.MinecraftClient

@LyraModule
object ChatCopyHandler {
    init {
        ClickChatEvent.EVENT.register(::onMessageClick)
    }

    private fun getMessageLineIndexByMouse(accessor: ChatHudAccessor, mouseX: Double, mouseY: Double): Int =
        accessor.invokeGetMessageLineIndex(
            accessor.invokeToChatLineX(mouseX),
            accessor.invokeToChatLineY(mouseY)
        )

    fun onMessageClick(data: MouseClickData, chatHud: ChatHudAccessor) {
        val (mouseX, mouseY, button) = data
        val client = MinecraftClient.getInstance()
        if (button != 0 || !isControlDown()) return

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
