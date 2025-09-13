package name.lyrablock.feature.display

import name.lyrablock.LyraModule
import name.lyrablock.util.DevUtils
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient

@LyraModule
object SpeedTracker {
    var speed: Int = 100

    init {
        ClientTickEvents.END_CLIENT_TICK.register(::tick)
        DevUtils.registerDrawTestText(600, 10, { "Speed: $speed" })
    }

    fun tick(client: MinecraftClient) {
        val player = client.player ?: return
        val sprinting = player.isSprinting
        val movementSpeed = player.movementSpeed.let {
            if (sprinting) it / 1.3f else it
        }

        speed = (movementSpeed * 1000).toInt()
    }
}
