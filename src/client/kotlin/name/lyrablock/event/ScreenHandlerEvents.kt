package name.lyrablock.event

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.slot.SlotActionType

object ScreenHandlerEvents {
    fun interface SlotClick {
        fun onSlotClick(slotIndex: Int, button: Int, actionType: SlotActionType, player: PlayerEntity)
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
