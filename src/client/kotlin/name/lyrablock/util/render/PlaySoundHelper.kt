package name.lyrablock.util.render

import net.minecraft.client.MinecraftClient
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents


object PlaySoundHelper {
    fun playSound(event: SoundEvent?, volume: Float, pitch: Float) {
        // Silently fail
        val player = MinecraftClient.getInstance().player ?: return

        player.playSound(event, volume, pitch)
    }

    fun ding() {
        playSound(SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(), 1.0f, 2.0f)
    }
}
