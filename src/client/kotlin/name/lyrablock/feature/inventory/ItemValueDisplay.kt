package name.lyrablock.feature.inventory

import name.lyrablock.LyraModule
import name.lyrablock.event.CancellableEventResult
import name.lyrablock.event.DrawEvents
import name.lyrablock.util.item.ItemUtils.getSkyBlockID
import net.minecraft.client.font.TextRenderer
import net.minecraft.item.ItemStack
import kotlin.uuid.ExperimentalUuidApi

@LyraModule
object ItemValueDisplay {
    init {
        DrawEvents.MODIFY_ITEM_TOOLTIP.register(::onDrawItemTooltip)
    }

    @OptIn(ExperimentalUuidApi::class)
    @Suppress("UNUSED_PARAMETER")
    fun onDrawItemTooltip(textRenderer: TextRenderer, stack: ItemStack, x: Int, y: Int): CancellableEventResult {
        if (stack.getSkyBlockID() != null) {
            return CancellableEventResult.PASS
        }

        return CancellableEventResult.CANCEL
    }
}
