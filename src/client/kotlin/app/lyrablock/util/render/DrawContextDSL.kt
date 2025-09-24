package app.lyrablock.util.render

import net.minecraft.client.gui.DrawContext

object DrawContextDSL {
    infix fun DrawContext.withPushMatrix(func: DrawContext.() -> Unit) {
        matrices.pushMatrix()
        apply(func)
        matrices.popMatrix()
    }

}
