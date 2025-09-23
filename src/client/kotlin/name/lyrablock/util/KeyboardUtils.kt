package app.lyrablock.util

import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.InputUtil
import org.lwjgl.glfw.GLFW

object KeyboardUtils {
    const val LEFT_MOUSE = -100
    const val RIGHT_MOUSE = -99
    const val MIDDLE_MOUSE = -98

    fun isKeyDown(id: Int) = when {
        id == -1 || id == 0 -> false
        else -> InputUtil.isKeyPressed(MinecraftClient.getInstance().window.handle, id)
    }
    private fun Int.isDown() = isKeyDown(this)

    fun isCommandDown() = GLFW.GLFW_KEY_LEFT_SUPER.isDown() || GLFW.GLFW_KEY_RIGHT_SUPER.isDown()
    fun isControlDown() = GLFW.GLFW_KEY_LEFT_CONTROL.isDown() || GLFW.GLFW_KEY_RIGHT_CONTROL.isDown()
    fun isModifierDown() = isCommandDown() || isControlDown()
    fun isShiftDown() = GLFW.GLFW_KEY_LEFT_SHIFT.isDown() || GLFW.GLFW_KEY_RIGHT_SHIFT.isDown()
}
