package app.lyrablock.orion.components

import app.lyrablock.orion.Constraints
import app.lyrablock.orion.OrionComponent
import app.lyrablock.orion.Size
import net.minecraft.client.gui.DrawContext

@Suppress("unused")
class SizedEmpty(val size: Size): OrionComponent {
    constructor(width: Number = 0, height: Number = 0) : this(Size(width, height))

    override fun measure(parentConstraints: Constraints): Size = size.coerceIn(parentConstraints)

    override fun render(context: DrawContext, size: Size) = Unit
}
