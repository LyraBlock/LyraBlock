package name.lyrablock.util

import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack

object ItemUtils {
    @Suppress("DEPRECATION")
    fun getCustomData(stack: ItemStack) = stack.get(DataComponentTypes.CUSTOM_DATA)?.nbt
}
