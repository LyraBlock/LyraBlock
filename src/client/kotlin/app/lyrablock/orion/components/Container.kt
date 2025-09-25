package app.lyrablock.orion.components

import app.lyrablock.orion.Constraints
import app.lyrablock.orion.OrionComponent
import app.lyrablock.orion.Size
import net.minecraft.client.gui.DrawContext

@Suppress("unused")
class Container(val modifier: BlockModifier = BlockModifier.DEFAULT, child: () -> OrionComponent? = { null }) : OrionComponent {
    val child = child.invoke()

    // Cache of the child's size. Initialized in measure().
    private var childMeasuredSize: Size? = null

    override fun measure(parentConstraints: Constraints): Size {
        // Take the most strict constraints
        val finalConstraints = parentConstraints intersect modifier.constraints

        // Measure size and cache
        val measuredSize = child?.measure(finalConstraints)
        childMeasuredSize = measuredSize

        val selfDesiredSize = measuredSize
            ?.let { finalConstraints.constrain(it) }
            ?: modifier.constraints.min

        return parentConstraints.constrain(selfDesiredSize)
    }

    override fun render(context: DrawContext, size: Size) {
        val child = child ?: return
        val measuredSize = childMeasuredSize ?: return
        child.render(context, measuredSize)
    }

    @Suppress("unused")
    data class BlockModifier(
        val constraints: Constraints = Constraints.NONE

    ) {
        companion object {
            val DEFAULT = BlockModifier()
        }
    }
}
