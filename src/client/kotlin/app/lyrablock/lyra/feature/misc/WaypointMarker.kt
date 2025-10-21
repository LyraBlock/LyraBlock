package app.lyrablock.lyra.feature.misc

import app.lyrablock.lyra.LyraModule
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.text.ClickEvent
import net.minecraft.text.Text

@LyraModule
object WaypointMarker {
    init {
        ClientReceiveMessageEvents.MODIFY_GAME.register(::modifyGameMessage)
    }

    val coordinatesRegex = Regex("""x:\s*(\d+(?:\.\d+)?)\s*,\s*y:\s*(\d+(?:\.\d+)?)\s*,\s*z:\s*(\d+(?:\.\d+)?)|\s*(\d+(?:\.\d+)?)[,\s]\s*(\d+(?:\.\d+)?)[,\s]\s*(\d+(?:\.\d+)?)""")

    fun modifyGameMessage(message: Text, overlay: Boolean): Text {
        if (overlay) return message
        val messageString = message.string
        if (!coordinatesRegex.containsMatchIn(messageString)) return message
//        if (ChatUtils.toHypixel(messageString) == null) return message
        val modifiedMessage = Text.empty()
        val match = coordinatesRegex.find(messageString) ?: return message
        val range = match.range
        modifiedMessage.append(messageString.take(range.first))
        val component = Text.literal(match.value)

        val style = component.style.withUnderline(true).withClickEvent(ClickEvent.SuggestCommand("/TODO waypoint add " + match.let {
            val x = it.groups[1]?.value ?: it.groups[4]?.value
            val y = it.groups[2]?.value ?: it.groups[5]?.value
            val z = it.groups[3]?.value ?: it.groups[6]?.value

            "$x $y $z"
        }))
        component.style = style
        modifiedMessage.append(component)
        modifiedMessage.append(messageString.substring(range.last + 1))

        return modifiedMessage
    }
}
