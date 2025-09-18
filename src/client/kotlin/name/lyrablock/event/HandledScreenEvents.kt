package name.lyrablock.event

import name.lyrablock.mixin.client.HandledScreenMixin
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.item.tooltip.TooltipData
import net.minecraft.screen.slot.Slot
import net.minecraft.text.Text
import net.minecraft.util.Identifier

object HandledScreenEvents {
    fun interface ModifyItemTooltip {
        fun onDrawItemTooltip(context: DrawContext, focusedSlot: Slot, textRenderer: TextRenderer, text: List<Text>, data: TooltipData?, x: Int, y: Int, texture: Identifier?): CancellableEventResult
    }

    @JvmField
    @InvokedBy(HandledScreenMixin::class)
    val MODIFY_ITEM_TOOLTIP = EventFactory.createArrayBacked(ModifyItemTooltip::class.java) { listeners ->
        ModifyItemTooltip { a, b, c, d, e, f, g, h ->
            listeners.forEach {
                val result = it.onDrawItemTooltip(a,b,c,d,e,f ,g, h)
                if (result.isCancel) {
                    return@ModifyItemTooltip CancellableEventResult.CANCEL
                }

            }

            CancellableEventResult.PASS
        }
    }!!
}
