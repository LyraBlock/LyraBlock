package app.lyrablock.lyra.feature.misc

import app.lyrablock.lyra.LyraModule
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.ClickEvent
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.HoverEvent

@LyraModule
object WaypointMarker {
    init {
        ClientReceiveMessageEvents.MODIFY_GAME.register(::modifyGameMessage)
    }

    // Matches `x: 1.0, y: 2.3, z: 3.2``.
    val namedCoordinatesRegex =
        Regex("""x:\s*(?<x>\d+(?:\.\d+)?)\s*,\s*y:\s*(?<y>\d+(?:\.\d+)?)\s*,\s*z:\s*(?<z>\d+(?:\.\d+)?)""")

    // Matches `1.0 2.3 3.2` or `1.0, 2.3, 3.2`, but not `1.0, 2.3, 3.2, 4.5`
    val separatedCoordinatesRegex =
        Regex("""\s*(?<x>\d+(?:\.\d+)?)[,\s]\s*(?<y>\d+(?:\.\d+)?)[,\s]\s*(?<z>\d+(?:\.\d+)?)(?![,\s]\s+\d)""")

    fun modifyGameMessage(message: Component, overlay: Boolean): Component {
        if (overlay) return message
        val messageString = message.string
        val match = namedCoordinatesRegex.find(messageString)
            ?: separatedCoordinatesRegex.find(messageString)
            ?: return message
        val range = match.range

        // Construct a message
        val modifiedMessage = Component.empty()
        modifiedMessage.append(messageString.take(range.first))
        val clickableComponent = Component.literal(match.value)

        val style = clickableComponent.style
            .withUnderlined(true)
            .withClickEvent(ClickEvent.SuggestCommand("/lyra:todo waypoint add " + match.let {
                val x = it.groups["x"]!!.value
                val y = it.groups["y"]!!.value
                val z = it.groups["z"]!!.value

                "$x $y $z"
            }))
            .withHoverEvent(HoverEvent.ShowText(
                Component.translatable("lyra.waypoint.add_tooltip").withStyle(ChatFormatting.YELLOW)
            ))
            .withColor(ChatFormatting.GOLD)
        clickableComponent.style = style
        modifiedMessage.append(clickableComponent)
        modifiedMessage.append(messageString.substring(range.last + 1))

        return modifiedMessage
    }
}
