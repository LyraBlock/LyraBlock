package app.lyrablock.lyra.util.render

import net.minecraft.client.Minecraft
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents


object PlaySoundHelper {
    fun playSound(event: SoundEvent?, volume: Float, pitch: Float) {
        // Silently fail
        val player = Minecraft.getInstance().player ?: return

        player.playSound(event, volume, pitch)
    }

    fun ding() {
        playSound(SoundEvents.NOTE_BLOCK_PLING.value(), 1.0f, 2.0f)
    }
}
