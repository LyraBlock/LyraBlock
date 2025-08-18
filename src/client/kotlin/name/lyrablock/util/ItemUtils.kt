package name.lyrablock.util

import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack

object ItemUtils {
    @Suppress("DEPRECATION")
    fun getCustomData(stack: ItemStack) = stack.get(DataComponentTypes.CUSTOM_DATA)?.nbt

    fun getLore(stack: ItemStack) = stack.get(DataComponentTypes.LORE)
}
