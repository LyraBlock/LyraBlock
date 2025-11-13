package app.lyrablock.orion.components

import app.lyrablock.orion.Constraints
import app.lyrablock.orion.OrionComponent
import app.lyrablock.orion.Size
import app.lyrablock.orion.math.CrossAxisAlignment
import app.lyrablock.orion.math.HorizontalAlignment
import app.lyrablock.orion.render.DrawContextDSL.withPushMatrix
import net.minecraft.client.gui.GuiGraphics

/**
 * A row that collects multiple components.
 * Used in a lambda with a `+` before every subcomponent.
 */
@Suppress("unused")
@CoreComponent
class Row(
    val gap: Number = 0,
    val horizontalAlignment: HorizontalAlignment = HorizontalAlignment.LEFT,
    val crossAxisAlignment: CrossAxisAlignment = CrossAxisAlignment.START,
    builder: SingletonCollectorScope<OrionComponent>.() -> Unit
) : OrionComponent {
    val builderScope = SingletonCollectorScope<OrionComponent>()

    val rawChildren = builderScope.apply(builder).collectChildren()
    data class MeasuredChild(val component: OrionComponent, var size: Size)
    lateinit var measuredChildren: List<MeasuredChild>

    override fun measure(parentConstraints: Constraints): Size {
        var totalWidth = 0
        var maxHeight = 0
        val gapFloat = gap.toFloat()

        val childConstraints = parentConstraints.minHeight(0)

        // Measure children and cache
        measuredChildren = rawChildren.map {
            val childSize = it.measure(childConstraints)
            // Sum width
            totalWidth += childSize.width
            // Maximize height
            if (childSize.height > maxHeight) {
                maxHeight = childSize.height
            }

            MeasuredChild(it, childSize)
        }

        // Add gap spacing (n-1 gaps for n children)
        if (rawChildren.size > 1) {
            totalWidth += ((rawChildren.size - 1) * gapFloat).toInt()
        }

        // Final size
        val finalSize = Size(totalWidth, maxHeight)

        return finalSize.coerceIn(parentConstraints)
    }

    override fun render(context: GuiGraphics, size: Size) {
        assert(::measuredChildren.isInitialized)
        
        val gapFloat = gap.toFloat()
        val totalChildrenWidth = measuredChildren.sumOf { it.size.width }
        val totalGapWidth = if (measuredChildren.size > 1) (measuredChildren.size - 1) * gapFloat else 0f
        val availableWidth = size.width.toFloat()
        
        // Calculate horizontal offset based on alignment
        val startX = when (horizontalAlignment) {
            HorizontalAlignment.LEFT -> 0f
            HorizontalAlignment.CENTER -> (availableWidth - totalChildrenWidth - totalGapWidth) / 2f
            HorizontalAlignment.RIGHT -> availableWidth - totalChildrenWidth - totalGapWidth
        }

        context.withPushMatrix {
            pose().translate(startX, 0f)
            
            measuredChildren.forEach { (component, cachedSize) ->
                context.withPushMatrix {
                    // Calculate vertical offset based on cross-axis alignment
                    val offsetY = when (crossAxisAlignment) {
                        CrossAxisAlignment.START -> 0f
                        CrossAxisAlignment.CENTER -> (size.height - cachedSize.height) / 2f
                        CrossAxisAlignment.END -> (size.height - cachedSize.height).toFloat()
                        CrossAxisAlignment.STRETCH -> 0f // TODO: Implement stretch by modifying child constraints
                    }
                    
                    pose().translate(0f, offsetY)
                    component.render(context, cachedSize)
                }
                pose().translate(cachedSize.width + gapFloat, 0f)
            }
        }
    }
}
