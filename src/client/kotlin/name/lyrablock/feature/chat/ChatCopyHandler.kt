package name.lyrablock.feature.chat

import name.lyrablock.InitializeWithClient
import name.lyrablock.event.ClickChatEvent
import name.lyrablock.event.MouseClickData
import name.lyrablock.mixin.client.ChatHudAccessor
import name.lyrablock.util.KeyboardUtils.isControlDown
import net.minecraft.client.MinecraftClient

@InitializeWithClient
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
