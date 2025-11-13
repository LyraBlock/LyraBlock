package app.lyrablock.lyra.event

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickType

object ScreenHandlerEvents {
    fun interface SlotClick {
        fun onSlotClick(slotIndex: Int, button: Int, actionType: ClickType, player: Player)
    }

    @JvmField
    val SLOT_CLICK = EventFactory.createArrayBacked(SlotClick::class.java) { listeners ->
        SlotClick { slotIndex, button, actionType, player ->
            listeners.forEach {
                it.onSlotClick(slotIndex, button, actionType, player)
            }
        }
    }!!
}
