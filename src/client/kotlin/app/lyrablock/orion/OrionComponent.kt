package app.lyrablock.orion

import app.lyrablock.orion.components.Container
import app.lyrablock.orion.components.VisuallyMove
import net.minecraft.client.gui.DrawContext

/**
 * OrionComponent is the basic ui element.
 * It handles the measurement and rendering itself.
 * @see OrionWidget
 */
@Suppress("unused")
interface OrionComponent {
    fun measure(parentConstraints: Constraints): Size

    fun render(context: DrawContext, size: Size)

    fun move(dx: Int = 0, dy: Int = 0) = VisuallyMove(dx, dy) { this }
    fun constrain(constraints: Constraints) = Container(constraints = constraints) { this }
}
