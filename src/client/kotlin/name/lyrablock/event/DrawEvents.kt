package name.lyrablock.event

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.font.TextRenderer
import net.minecraft.item.ItemStack

object DrawEvents {
    fun interface ModifyTooltip {
        fun onDrawTooltip(textRenderer: TextRenderer, stack: ItemStack, x: Int, y: Int) : CancellableEventResult
    }

    @JvmField
    val MODIFY_ITEM_TOOLTIP = EventFactory.createArrayBacked(ModifyTooltip::class.java) { listeners ->
        ModifyTooltip { textRenderer, stack, x, y ->
            listeners.forEach {
                val result = it.onDrawTooltip(textRenderer, stack, x, y)
                if (result == CancellableEventResult.CANCEL) {
                    return@ModifyTooltip CancellableEventResult.CANCEL
                }
            }

            CancellableEventResult.PASS
        }
    }!!
}
