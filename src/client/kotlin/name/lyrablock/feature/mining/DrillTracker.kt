package name.lyrablock.feature.mining

import kotlinx.serialization.Serializable
import name.lyrablock.util.item.ItemUtils.getCustomData
import name.lyrablock.util.item.ItemUtils.getLoreLines
import name.lyrablock.util.item.ItemUtils.getSkyBlockID
import name.lyrablock.util.item.ItemUtils.getSkyBlockUUID
import name.lyrablock.util.math.NumberUtils
import net.minecraft.item.ItemStack
import kotlin.jvm.optionals.getOrNull
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object DrillTracker {
    @Serializable
    /**
     * We only take the critical data we will use.
     *
     * @param uuid The SkyBlock UUID of the item.
     * @param plainMiningSpeed The plain mining speed, that is, without Pickaxe Ability or Flowstate.
     * @param hasBlueCheese If the drill has Blue Cheese Goblin Omelette installed.
     */
    data class DrillData(
        val uuid: Uuid,
        val plainMiningSpeed: Int,
        val hasBlueCheese: Boolean,
        val fuelTank: FuelTank,
        val flowStateLevel: Int
    ) {
////         This is used to remove drill data from deleted profiles.
//        val profile = Uuid.random()
    }

    enum class FuelTank(val cooldownModifier: Double) {
        PERFECTLY_CUT(0.8),
        GEMSTONE(0.94),
        TITANIUM_INFUSED(0.96),
        MITHRIL_INFUSED(0.98),
        NOT_INSTALLED(1.0)
    }

    val PLACEHOLDER_DRILL = DrillData(Uuid.NIL, 0, false, FuelTank.NOT_INSTALLED, 0)

    val MINING_SPEED_REGEX = Regex("""^§7Mining Speed: §a\+([0-9,]+).*""")

//    var drillDataset = mutableMapOf<Uuid, DrillData>()

    fun extract(stack: ItemStack): DrillData? {
        val id = stack.getSkyBlockID() ?: return null
        if (!id.contains("DRILL")) return null
        val uuid = stack.getSkyBlockUUID() ?: return null
        val customData = stack.getCustomData()!!

        val fuelTank = when (customData.getString("drill_part_fuel_tank").getOrNull()) {
            "perfectly_cut_fuel_tank" -> FuelTank.PERFECTLY_CUT
            "gemstone_fuel_tank" -> FuelTank.GEMSTONE
            "titanium_infused_fuel_tank" -> FuelTank.TITANIUM_INFUSED
            "mithril_infused_fuel_tank" -> FuelTank.MITHRIL_INFUSED
            else -> FuelTank.NOT_INSTALLED
        }

        val hasBlueCheese =
            customData.getString("drill_part_upgrade_module").getOrNull() == "goblin_omelette_blue_cheese"

        val flowStateLevel = customData.getCompound("enchantments").get().getInt("ultimate_flowstate").get()

        val loreLinesString = stack.getLoreLines()?.map { it.string } ?: return null

        // Find the line with mining speed.
        val miningSpeed: Int = loreLinesString
            .find { it.startsWith("§7Mining Speed: ") }?.let { miningSpeedLine ->
                MINING_SPEED_REGEX.matchEntire(miningSpeedLine).let { matchResult ->
                    // Here we assert the match will not fail.
                    NumberUtils.parseLocaleInt(matchResult!!.groups[1]!!.value)
                }
                // If somehow failed to find such a line, fallback to 0.
            } ?: 0

        return DrillData(
            uuid,
            miningSpeed,
            hasBlueCheese,
            fuelTank,
            flowStateLevel,
        )
    }

}
