package app.lyrablock.orion.components

import app.lyrablock.lyra.util.render.MatrixStackDSL.uniformScale
import app.lyrablock.orion.Constraints
import app.lyrablock.orion.OrionComponent
import app.lyrablock.orion.Size
import app.lyrablock.orion.render.DrawContextDSL.withPushMatrix
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.network.chat.Component
import net.minecraft.util.FormattedCharSequence

@Suppress("unused")
class TextView(val text: FormattedCharSequence, val style: Style = Style.DEFAULT) : OrionComponent {
    constructor(text: Component, style: Style = Style.DEFAULT) : this(text.visualOrderText, style)
    constructor(text: String, style: Style = Style.DEFAULT) : this(Component.nullToEmpty(text), style)

    val textRenderer = Minecraft.getInstance().font!!
    val fontHeight = textRenderer.lineHeight

    val originSize = Size(textRenderer.width(text) * style.scale, fontHeight * style.scale)

    override fun measure(parentConstraints: Constraints): Size {
        return originSize.coerceIn(parentConstraints)
    }

    override fun render(context: GuiGraphics, size: Size) {
        context.withPushMatrix {
            pose().uniformScale(style.scale)
            context.drawString(textRenderer, text, 0, 0, style.color, style.shadowed)
        }
    }

    data class Style(
        var shadowed: Boolean = true,
        var scale: Float = 1f,
        var color: Int = 0xFFFFFFFF.toInt()
    ) {
        fun styled(text: String) = TextView(text)
        fun styled(text: Component) = TextView(text)

        companion object {
            val DEFAULT = Style()
        }
    }
}
