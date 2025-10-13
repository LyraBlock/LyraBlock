package app.lyrablock.orion.components

import app.lyrablock.orion.Constraints
import app.lyrablock.orion.OrionComponent
import app.lyrablock.orion.Size
import net.minecraft.client.gui.DrawContext

/**
 * Hides the inner component. **The space will be still occupied.**
 */
@Suppress("unused")
@CoreComponent
class Hide(val show: Boolean = false, child: () -> OrionComponent) : OrionComponent {
    val child by lazy(child)

    override fun measure(parentConstraints: Constraints): Size = child.measure(parentConstraints)

    override fun render(context: DrawContext, size: Size) {
        if (show) child.render(context, size)
    }
}
