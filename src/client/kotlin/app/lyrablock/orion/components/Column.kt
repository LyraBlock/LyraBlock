package app.lyrablock.orion.components

import app.lyrablock.lyra.util.render.DrawContextDSL.withPushMatrix
import app.lyrablock.lyra.util.render.MatrixStackDSL.translate
import app.lyrablock.orion.Constraints
import app.lyrablock.orion.OrionComponent
import app.lyrablock.orion.Size
import net.minecraft.client.gui.DrawContext

@Suppress("unused")
class Column(builder: CollectionComponentBuilderScope.() -> Unit) : OrionComponent {
    val builderScope = CollectionComponentBuilderScope()

    val rawChildren = builderScope.apply(builder).getChildren()
    data class MeasuredChild(val component: OrionComponent, var size: Size)
    lateinit var measuredChildren: List<MeasuredChild>

    override fun measure(parentConstraints: Constraints): Size {
        var totalHeight = 0
        var maxWidth = 0

        val childConstraints = parentConstraints.minHeight(0)

        // Measure children and cache
        measuredChildren = rawChildren.map {
            val childSize = it.measure(childConstraints)
            // Sum height
            totalHeight += childSize.height
            // Maximize width
            if (childSize.width > maxWidth) {
                maxWidth = childSize.width
            }

            MeasuredChild(it, childSize)
        }

        // Final size
        val finalSize = Size(maxWidth, totalHeight)

        return finalSize.coerceIn(parentConstraints)
    }

    override fun render(context: DrawContext, size: Size) {
        assert(::measuredChildren.isInitialized)

        context.withPushMatrix {
            measuredChildren.forEach { (component, cachedSize) ->
                component.render(context, cachedSize)
                matrices.translate(0, cachedSize.height)
            }
        }
    }
}
