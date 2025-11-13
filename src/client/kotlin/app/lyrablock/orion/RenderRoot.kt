package app.lyrablock.orion

import app.lyrablock.lyra.util.render.MatrixStackDSL.translate
import app.lyrablock.orion.render.DrawContextDSL.withPushMatrix
import net.minecraft.client.gui.GuiGraphics

class RenderRoot(val component: OrionComponent) {
    // Cache the size to avoid recalculating on each access
    private val size by lazy { component.measure(Constraints.NONE) }

    fun render(context: GuiGraphics, x: Int, y: Int) {
        context.withPushMatrix {
            pose().translate(x, y)
            component.render(context, size)
        }
    }
}
