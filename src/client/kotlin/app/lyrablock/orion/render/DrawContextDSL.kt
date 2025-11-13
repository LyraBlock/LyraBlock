package app.lyrablock.orion.render

import app.lyrablock.orion.render.ScreenRectDSL.x1
import app.lyrablock.orion.render.ScreenRectDSL.x2
import app.lyrablock.orion.render.ScreenRectDSL.y1
import app.lyrablock.orion.render.ScreenRectDSL.y2
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.navigation.ScreenRectangle

object DrawContextDSL {
    infix fun GuiGraphics.withPushMatrix(func: GuiGraphics.() -> Unit) {
        pose().pushMatrix()
        apply(func)
        pose().popMatrix()
    }

    fun GuiGraphics.withScissor(x1: Int, y1: Int, x2: Int, y2: Int, func: GuiGraphics.() -> Unit) {
        enableScissor(x1, y1, x2, y2)
        apply(func)
        disableScissor()
    }

    fun GuiGraphics.withScissor(rect: ScreenRectangle, func: GuiGraphics.() -> Unit) =
        withScissor(rect.x1, rect.y1, rect.x2, rect.y2, func)

    fun GuiGraphics.withScissorIf(cond: Boolean, x1: Int, y1: Int, x2: Int, y2: Int, func: GuiGraphics.() -> Unit) {
        if (cond) enableScissor(x1, y1, x2, y2)
        apply(func)
        if (cond) disableScissor()
    }
    fun GuiGraphics.withScissorIf(cond: Boolean, rect: ScreenRectangle, func: GuiGraphics.() -> Unit) =
        withScissorIf(cond, rect.x1, rect.y1, rect.x2, rect.y2, func)
}
