package app.lyrablock.orion.render

import app.lyrablock.orion.render.ScreenRectDSL.x1
import app.lyrablock.orion.render.ScreenRectDSL.x2
import app.lyrablock.orion.render.ScreenRectDSL.y1
import app.lyrablock.orion.render.ScreenRectDSL.y2
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.ScreenRect

object DrawContextDSL {
    infix fun DrawContext.withPushMatrix(func: DrawContext.() -> Unit) {
        matrices.pushMatrix()
        apply(func)
        matrices.popMatrix()
    }

    fun DrawContext.withScissor(x1: Int, y1: Int, x2: Int, y2: Int, func: DrawContext.() -> Unit) {
        enableScissor(x1, y1, x2, y2)
        apply(func)
        disableScissor()
    }

    fun DrawContext.withScissor(rect: ScreenRect, func: DrawContext.() -> Unit) =
        withScissor(rect.x1, rect.y1, rect.x2, rect.y2, func)

    fun DrawContext.withScissorIf(cond: Boolean, x1: Int, y1: Int, x2: Int, y2: Int, func: DrawContext.() -> Unit) {
        if (cond) enableScissor(x1, y1, x2, y2)
        apply(func)
        if (cond) disableScissor()
    }
    fun DrawContext.withScissorIf(cond: Boolean, rect: ScreenRect, func: DrawContext.() -> Unit) =
        withScissorIf(cond, rect.x1, rect.y1, rect.x2, rect.y2, func)
}
