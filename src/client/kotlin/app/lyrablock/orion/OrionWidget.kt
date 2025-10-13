package app.lyrablock.orion

import net.minecraft.client.gui.DrawContext

/**
 * A high-level implementation.
 * A widget needs only implement `build` method, basically a warp of low-level components.
 * @see OrionComponent
 */
@Suppress("unused")
abstract class OrionWidget : OrionComponent {
    abstract fun build(): OrionComponent
    val built: OrionComponent by lazy { build() }

    override fun measure(parentConstraints: Constraints): Size = built.measure(parentConstraints)

    override fun render(context: DrawContext, size: Size) = built.render(context, size)
}
