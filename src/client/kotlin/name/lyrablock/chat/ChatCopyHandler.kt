package name.lyrablock.chat

import name.lyrablock.event.ClickChatEventFactory
import name.lyrablock.mixin.client.ChatHudAccessor
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

object ChatCopyHandler {
    init {
        ClickChatEventFactory.register(::onMessageClick)
    }

    // Get chatX and chatY by mouse position, then get messageLineIndex.
    fun getMessageLineIndexByMouse(accessor: ChatHudAccessor, mouseX: Double, mouseY: Double): Int {
        val chatX = accessor.invokeToChatLineX(mouseX)
        val chatY = accessor.invokeToChatLineY(mouseY)
        return accessor.invokeGetMessageLineIndex(chatX, chatY)
    }

    fun onMessageClick(event: ClickChatEventFactory.ClickChatEvent) {
        val (mouseX, mouseY, button, chatHud) = event


        val client = MinecraftClient.getInstance()
        val windowHandle = client.window.handle

        val isControlPressed = InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_LEFT_CONTROL)
                || InputUtil.isKeyPressed(windowHandle, GLFW.GLFW_KEY_RIGHT_CONTROL)

        if (button != 0 || !isControlPressed) return

        // The accessor exposed toChatLine, etc.
        val lineIndex = getMessageLineIndexByMouse(chatHud, mouseX, mouseY)
        // Somehow StackOverflow happens
        if (lineIndex < 0) return
        val messages = chatHud.getVisibleMessages()
        if (lineIndex >= messages.size) return
        val message = messages[lineIndex]
        val builder = StringBuilder()
        message.content().accept { index, style, codePoint ->
            builder.appendCodePoint(codePoint)
            true
        }

        client.keyboard.clipboard = builder.toString()
    }
}