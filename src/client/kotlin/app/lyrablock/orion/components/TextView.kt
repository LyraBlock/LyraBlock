package app.lyrablock.orion.components

import app.lyrablock.lyra.util.render.MatrixStackDSL.uniformScale
import app.lyrablock.orion.Constraints
import app.lyrablock.orion.OrionComponent
import app.lyrablock.orion.Size
import app.lyrablock.orion.render.DrawContextDSL.withPushMatrix
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.DrawContext
import net.minecraft.text.OrderedText
import net.minecraft.text.Text

@Suppress("unused")
class TextView(val text: OrderedText, val style: Style = Style.DEFAULT) : OrionComponent {
    constructor(text: Text, style: Style = Style.DEFAULT) : this(text.asOrderedText(), style)
    constructor(text: String, style: Style = Style.DEFAULT) : this(Text.of(text), style)

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

    data class Style(
        var shadowed: Boolean = true,
        var scale: Float = 1f,
        var color: Int = 0xFFFFFFFF.toInt()
    ) {
        fun styled(text: String) = TextView(text)
        fun styled(text: Text) = TextView(text)

        companion object {
            val DEFAULT = Style()
        }
    }
}
