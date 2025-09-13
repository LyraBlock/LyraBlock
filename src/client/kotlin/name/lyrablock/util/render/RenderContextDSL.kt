package name.lyrablock.util.render

import net.minecraft.client.util.math.MatrixStack

object RenderContextDSL {
    infix fun MatrixStack.withPush(func: MatrixStack.() -> Unit) {
        push()
        apply(func)
        pop()
    }
}
