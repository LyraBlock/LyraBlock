package name.lyrablock.util.render

import net.minecraft.client.gui.DrawContext

object DrawContextDSL {
    infix fun DrawContext.withPushMatrix(func: DrawContext.() -> Unit) {
        matrices.push()
        apply(func)
        matrices.pop()
    }

}
