// filepath: d:\LyraBlock\LyraBlock\src\client\kotlin\app\lyrablock\orion\components\Container.kt
package app.lyrablock.orion.components

import app.lyrablock.lyra.util.render.MatrixStackDSL.translate
import app.lyrablock.orion.Constraints
import app.lyrablock.orion.OrionComponent
import app.lyrablock.orion.Size
import app.lyrablock.orion.math.EdgeInsets
import app.lyrablock.orion.math.Gradient
import app.lyrablock.orion.render.DrawContextDSL.withPushMatrix
import app.lyrablock.orion.render.DrawContextDSL.withScissorIf
import net.minecraft.client.gui.DrawContext

@Suppress("unused")
@CoreComponent
class Container(
    val constraints: Constraints = Constraints.NONE,
    val scissor: Boolean = false,
    val color: Gradient? = null,
    val padding: EdgeInsets = EdgeInsets.NONE,
    child: () -> OrionComponent? = { null }
) : OrionComponent {
    val child = child.invoke()

    // Cache of the child's size. Initialized in measure().
    private var childMeasuredSize: Size? = null

    override fun measure(parentConstraints: Constraints): Size {
        // Take the most strict constraints
        val finalConstraints = parentConstraints intersect constraints


        // Build child constraints by subtracting padding from available space (never negative)
        val childMin = Size(
            (finalConstraints.min.width - padding.horizontal).coerceAtLeast(0),
            (finalConstraints.min.height - padding.vertical).coerceAtLeast(0)
        )
        val childMax = Size(
            (finalConstraints.max.width - padding.horizontal).coerceAtLeast(0),
            (finalConstraints.max.height - padding.vertical).coerceAtLeast(0)
        )
        val childConstraints = Constraints(childMin, childMax)

        // Measure size and cache (childMeasuredSize does NOT include padding)
        val measuredSize = child?.measure(childConstraints)
        childMeasuredSize = measuredSize

        // Add padding back to the measured child size to produce this container's desired size.
        val selfDesiredSize = measuredSize
            ?.let { Size(it.width + padding.horizontal, it.height + padding.vertical).let { finalConstraints.constrain(it) } }
            ?: constraints.min

        return parentConstraints.constrain(selfDesiredSize)
    }

    override fun render(context: DrawContext, size: Size) {
        val child = child ?: return
        val measuredSize = childMeasuredSize ?: return

        // Keep scissor on the full container area, then translate the matrix for padding before rendering child
        context.withScissorIf(scissor, rect = size.atScreenOrigin()) {
            this@Container.color?.let {
                context.fillGradient(0, 0, size.width, size.height, it.color1.argb, it.color2.argb)
            }

            context.withPushMatrix {
                matrices.translate(padding.left, padding.top)
                child.render(context, measuredSize)
            }
        }
    }
}

