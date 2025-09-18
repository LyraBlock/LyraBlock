package name.lyrablock.event

import name.lyrablock.mixin.client.ItemStackMixin
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.item.ItemStack

@InvokedBy(ItemStackMixin::class)
object ItemUseEvents {
    fun interface Use {
        fun onUse(itemStack: ItemStack)
    }

    fun interface ModifyUse {
        fun onModifyUse(itemStack: ItemStack): CancellableEventResult
    }

    @JvmField
    val USE = EventFactory.createArrayBacked(Use::class.java) { listeners ->
        Use { itemStack ->
            listeners.forEach {
                it.onUse(itemStack)
            }
        }
    }!!

    @JvmField
    val MODIFY_USE = EventFactory.createArrayBacked(ModifyUse::class.java) { listeners ->
        ModifyUse { itemStack ->
            listeners.forEach {
                val result = it.onModifyUse(itemStack)
                if (result == CancellableEventResult.CANCEL) {
                    return@ModifyUse CancellableEventResult.CANCEL
                }
            }
            CancellableEventResult.PASS
        }
    }!!
}
