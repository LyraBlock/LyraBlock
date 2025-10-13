package app.lyrablock.orion.render

import net.minecraft.client.gui.ScreenRect

object ScreenRectDSL {
    val ScreenRect.x1 get() = this.position.x
    val ScreenRect.y1 get() = this.position.y
    val ScreenRect.x2 get() = x1 + width
    val ScreenRect.y2 get() = y1 + height
}
