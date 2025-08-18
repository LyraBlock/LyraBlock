package name.lyrablock.util

import net.minecraft.client.MinecraftClient
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack

object ItemUtils {
    @Suppress("DEPRECATION")
    fun getCustomData(stack: ItemStack) = stack.get(DataComponentTypes.CUSTOM_DATA)?.nbt
    fun getLore(stack: ItemStack) = stack.get(DataComponentTypes.LORE)

    val selectedStack: ItemStack? get() = MinecraftClient.getInstance().player?.inventory?.selectedStack
}
