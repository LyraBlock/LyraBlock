package name.lyrablock.util.item

import net.minecraft.client.MinecraftClient
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object ItemUtils {
    @Suppress("DEPRECATION")
    fun ItemStack.getCustomData() = this.get(DataComponentTypes.CUSTOM_DATA)?.nbt

    fun ItemStack.getLore() = this.get(DataComponentTypes.LORE)
    fun ItemStack.getLoreLines() = this.getLore()?.lines

    fun ItemStack.getSkyBlockID() : String? = this.getCustomData()?.getString("id")?.get()
    @OptIn(ExperimentalUuidApi::class)
    fun ItemStack.getSkyBlockUUID() : Uuid? = this.getCustomData()?.getString("uuid")?.get()?.let { Uuid.Companion.parse(it) }

    fun ItemStack.getEnchantmentLevel(enchantment: String) : Int? {
        val enchantmentData = this.getCustomData()?.getCompound("enchantments")?.get() ?: return null
        return enchantmentData.getInt(enchantment).get()
    }

    val selectedStack: ItemStack? get() = MinecraftClient.getInstance().player?.inventory?.selectedStack
}
