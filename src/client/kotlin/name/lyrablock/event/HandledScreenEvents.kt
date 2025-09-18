package name.lyrablock.event

import name.lyrablock.mixin.client.HandledScreenMixin
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.item.tooltip.TooltipData
import net.minecraft.screen.slot.Slot
import net.minecraft.text.Text
import net.minecraft.util.Identifier

@InvokedBy(HandledScreenMixin::class)
object HandledScreenEvents {
    fun interface ModifyItemTooltip {
        fun onDrawItemTooltip(
            context: DrawContext,
            focusedSlot: Slot,
            textRenderer: TextRenderer,
            text: List<Text>,
            data: TooltipData?,
            x: Int,
            y: Int,
            texture: Identifier?
        ): CancellableEventResult
    }

    fun interface MouseScrolled {
        fun onMouseScrolled(mouseX: Double, mouseY: Double, horizontalAmount: Double, verticalAmount: Double)
    }

    @JvmField
    val MODIFY_ITEM_TOOLTIP = EventFactory.createArrayBacked(ModifyItemTooltip::class.java) { listeners ->
        ModifyItemTooltip { context, focusedSlot, textRenderer, text, data, x, y, texture ->
            listeners.forEach {
                val result = it.onDrawItemTooltip(context, focusedSlot, textRenderer, text, data, x, y, texture)
                if (result.isCancel) {
                    return@ModifyItemTooltip CancellableEventResult.CANCEL
                }

            }

            CancellableEventResult.PASS
        }
    }!!

    @JvmField
    val MOUSE_SCROLLED = EventFactory.createArrayBacked(MouseScrolled::class.java) { listeners ->
        MouseScrolled { mouseX, mouseY, horizontalAmount, verticalAmount ->
            listeners.forEach {
                it.onMouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount)
            }
        }
    }
}
