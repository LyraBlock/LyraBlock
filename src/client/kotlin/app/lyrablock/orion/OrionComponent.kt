package app.lyrablock.orion

import net.minecraft.client.gui.DrawContext

/**
 * OrionComponent is the basic ui element.
 * It handles the measurement and rendering itself.
 */
interface OrionComponent {
    fun measure(parentConstraints: Constraints): Size

    fun render(context: DrawContext, size: Size)
}
