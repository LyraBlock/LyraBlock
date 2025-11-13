package app.lyrablock.orion.render

import net.minecraft.client.gui.navigation.ScreenRectangle

object ScreenRectDSL {
    val ScreenRectangle.x1 get() = this.position.x
    val ScreenRectangle.y1 get() = this.position.y
    val ScreenRectangle.x2 get() = x1 + width
    val ScreenRectangle.y2 get() = y1 + height
}
