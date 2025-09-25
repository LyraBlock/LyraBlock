package app.lyrablock.orion.components

import app.lyrablock.lyra.util.render.DrawContextDSL.withPushMatrix
import app.lyrablock.lyra.util.render.MatrixStackDSL.uniformScale
import app.lyrablock.orion.Constraints
import app.lyrablock.orion.OrionComponent
import app.lyrablock.orion.Size
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.OrderedText
import net.minecraft.text.Text

@Suppress("unused")
class TextView(val text: OrderedText, val style: TextStyle = TextStyle.DEFAULT) : OrionComponent {
    constructor(text: Text, style: TextStyle = TextStyle.DEFAULT) : this(text.asOrderedText(), style)
    constructor(text: String, style: TextStyle = TextStyle.DEFAULT) : this(Text.of(text), style)

    val textRenderer = MinecraftClient.getInstance().textRenderer!!
    val fontHeight = textRenderer.fontHeight

    val originSize = Size(textRenderer.getWidth(text) * style.scale, fontHeight * style.scale)

    override fun measure(parentConstraints: Constraints): Size {
        return originSize.coerceIn(parentConstraints)
    }

    override fun render(context: DrawContext, size: Size) {
        context.withPushMatrix {
            matrices.uniformScale(style.scale)
            context.drawText(textRenderer, text, 0, 0, style.color, style.shadowed)
        }
    }

    data class TextStyle(
        var shadowed: Boolean = true,
        var scale: Float = 1f,
        var color: Int = 0xFFFFFFFF.toInt()
    ) {
        companion object {
            val DEFAULT = TextStyle()
        }
    }
}
