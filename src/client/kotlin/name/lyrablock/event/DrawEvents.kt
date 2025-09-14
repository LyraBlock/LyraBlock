package name.lyrablock.event

import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.item.ItemStack

object DrawEvents {
    fun interface ModifyTooltip {
        fun onDrawTooltip(context: DrawContext, textRenderer: TextRenderer, stack: ItemStack, x: Int, y: Int) : CancellableEventResult
    }

    @JvmField
    val MODIFY_ITEM_TOOLTIP = EventFactory.createArrayBacked(ModifyTooltip::class.java) { listeners ->
        ModifyTooltip { context, textRenderer, stack, x, y ->
            listeners.forEach {
                val result = it.onDrawTooltip(context, textRenderer, stack, x, y)
                if (result == CancellableEventResult.CANCEL) {
                    return@ModifyTooltip CancellableEventResult.CANCEL
                }
            }

            CancellableEventResult.PASS
        }
    }!!
}
