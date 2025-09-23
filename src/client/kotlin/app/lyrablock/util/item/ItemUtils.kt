package app.lyrablock.util.item

import net.minecraft.client.MinecraftClient
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.ItemStack
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

object ItemUtils {
    /** Returns the custom NBT data of the ItemStack, or null if absent. */
    @Suppress("DEPRECATION")
    fun ItemStack.getCustomData() = get(DataComponentTypes.CUSTOM_DATA)?.nbt

    /** Returns the lore component of the ItemStack, or null if absent. */
    fun ItemStack.getLore() = get(DataComponentTypes.LORE)

    /** Returns the lore lines of the ItemStack, or null if absent. */
    fun ItemStack.getLoreLines() = getLore()?.lines

    /** Returns the SkyBlock ID from custom data, or null if absent. */
    fun ItemStack.getSkyBlockID() = getCustomData()?.getString("id")?.orElse(null)

    /** Returns the SkyBlock UUID from custom data, or null if absent. */
    @OptIn(ExperimentalUuidApi::class)
    fun ItemStack.getSkyBlockUUID() = getCustomData()?.getString("uuid")?.orElse(null)?.let(Uuid::parse)

    /** Returns the level of the given enchantment, or null if not present. */
    fun ItemStack.getEnchantmentLevel(enchantment: String): Int? =
        getCustomData()?.getCompound("enchantments")?.get()?.getInt(enchantment)?.get()

    /** Returns the currently selected ItemStack, or null if unavailable. */
    val selectedStack: ItemStack?
        get() = MinecraftClient.getInstance().player?.inventory?.selectedStack
}
