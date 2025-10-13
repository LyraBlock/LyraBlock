package app.lyrablock.orion.components

import app.lyrablock.lyra.util.render.MatrixStackDSL.translate
import app.lyrablock.orion.Constraints
import app.lyrablock.orion.OrionComponent
import app.lyrablock.orion.Size
import app.lyrablock.orion.render.DrawContextDSL.withPushMatrix
import net.minecraft.client.gui.DrawContext

/**
 * Visually moves the element, but does not affect the actual logic
 */
@CoreComponent
class VisuallyMove(val dx: Number = 0, val dy: Number = 0, child: () -> OrionComponent) : OrionComponent {
    val child by lazy(child)

    override fun measure(parentConstraints: Constraints): Size = child.measure(parentConstraints)

    override fun render(context: DrawContext, size: Size) {
        context.withPushMatrix {
            matrices.translate(dx, dy)
            child.render(context, size)
        }
    }

}
