package app.lyrablock.feature.mining

import app.lyrablock.LyraModule
import app.lyrablock.util.DevUtils
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.text.Text
import kotlin.time.Duration.Companion.seconds

@LyraModule
object SnailHelper {
    init {
        ClientReceiveMessageEvents.GAME.register(::onMessage)

        DevUtils.registerDrawTestText(10, 30, { "Rogue Sword: ${(ROGUE_SWORD_DURATION - (rogueSwordActivation - Clock.System.now())).inWholeSeconds.coerceAtLeast(0)}" })
    }

    var rogueSwordActivation = Instant.DISTANT_PAST
    val ROGUE_SWORD_DURATION = 30.seconds

    private fun onMessage(message: Text, overlay: Boolean)  {
        if (!overlay) return
        val message = message.string
        if (message.contains("(§6Speed Boost§b)")) {
            rogueSwordActivation = Clock.System.now()
        }
    }
}
