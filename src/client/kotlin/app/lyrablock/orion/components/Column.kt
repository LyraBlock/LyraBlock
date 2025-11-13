package app.lyrablock.orion.components

import app.lyrablock.orion.Constraints
import app.lyrablock.orion.OrionComponent
import app.lyrablock.orion.Size
import app.lyrablock.orion.math.CrossAxisAlignment
import app.lyrablock.orion.math.VerticalAlignment
import app.lyrablock.orion.render.DrawContextDSL.withPushMatrix
import net.minecraft.client.gui.GuiGraphics

/**
 * A column that collects multiple components.
 * Used in a lambda with a `+` before every subcomponent.
 */
@Suppress("unused")
@CoreComponent
class Column(
    val gap: Number = 0,
    val verticalAlignment: VerticalAlignment = VerticalAlignment.TOP,
    val crossAxisAlignment: CrossAxisAlignment = CrossAxisAlignment.START,
    builder: SingletonCollectorScope<OrionComponent>.() -> Unit
) : OrionComponent {
    val builderScope = SingletonCollectorScope<OrionComponent>()

    val rawChildren = builderScope.apply(builder).collectChildren()
    data class MeasuredChild(val component: OrionComponent, var size: Size)
    lateinit var measuredChildren: List<MeasuredChild>

    override fun measure(parentConstraints: Constraints): Size {
        var totalHeight = 0
        var maxWidth = 0
        val gapFloat = gap.toFloat()

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

        // Add gap spacing (n-1 gaps for n children)
        if (rawChildren.size > 1) {
            totalHeight += ((rawChildren.size - 1) * gapFloat).toInt()
        }

        // Final size
        val finalSize = Size(maxWidth, totalHeight)

        return finalSize.coerceIn(parentConstraints)
    }

    override fun render(context: GuiGraphics, size: Size) {
        assert(::measuredChildren.isInitialized)
        
        val gapFloat = gap.toFloat()
        val totalChildrenHeight = measuredChildren.sumOf { it.size.height }
        val totalGapHeight = if (measuredChildren.size > 1) (measuredChildren.size - 1) * gapFloat else 0f
        val availableHeight = size.height.toFloat()
        
        // Calculate vertical offset based on alignment
        val startY = when (verticalAlignment) {
            VerticalAlignment.TOP -> 0f
            VerticalAlignment.CENTER -> (availableHeight - totalChildrenHeight - totalGapHeight) / 2f
            VerticalAlignment.BOTTOM -> availableHeight - totalChildrenHeight - totalGapHeight
        }

        context.withPushMatrix {
            pose().translate(0f, startY)
            
            measuredChildren.forEach { (component, cachedSize) ->
                context.withPushMatrix {
                    // Calculate horizontal offset based on cross-axis alignment
                    val offsetX = when (crossAxisAlignment) {
                        CrossAxisAlignment.START -> 0f
                        CrossAxisAlignment.CENTER -> (size.width - cachedSize.width) / 2f
                        CrossAxisAlignment.END -> (size.width - cachedSize.width).toFloat()
                        CrossAxisAlignment.STRETCH -> 0f // TODO: Implement stretch by modifying child constraints
                    }
                    
                    pose().translate(offsetX, 0f)
                    component.render(context, cachedSize)
                }
                pose().translate(0f, cachedSize.height + gapFloat)
            }
        }
    }
}
