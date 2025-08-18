package name.lyrablock.feature.mining

import name.lyrablock.LyraModule
import name.lyrablock.feature.pet.PetTracker
import name.lyrablock.util.AbuseBoolean.toInt
import name.lyrablock.util.DevUtils
import name.lyrablock.util.TripleInt
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.text.Text

@LyraModule
object PickaxeAbilityTracker {
    /**
     * @param cuteName The display name.
     * @param cooldown The ability cooldown, corresponds to 3 levels.
     */
    data class PickaxeAbilityData(val cuteName: String, val cooldown: TripleInt)

    val MINING_SPEED_BOOST = PickaxeAbilityData("Mining Speed Boost", TripleInt.triple(120))
    val ANOMALOUS_DESIRE = PickaxeAbilityData("Anomalous Desire", TripleInt(120, 110, 100))
    val PICKOBULUS = PickaxeAbilityData("Pickobulus", TripleInt(40, 50, 60))
    val MANIAC_MINER = PickaxeAbilityData("Maniac Miner", TripleInt(25, 30, 35))
    val GEMSTONE_INFUSION = PickaxeAbilityData("Gemstone Infusion", TripleInt(120, 120, 120))
    val SHEER_FORCE = PickaxeAbilityData("Sheer Force", TripleInt(120, 120, 120))
    val ABILITY_LIST = arrayOf(
        MINING_SPEED_BOOST,
        ANOMALOUS_DESIRE,
        PICKOBULUS,
        MANIAC_MINER,
        GEMSTONE_INFUSION,
        SHEER_FORCE,
    )

    private val COOLDOWN_REGEX = Regex("""^§cYour Pickaxe ability is on cooldown for ([0-9]+)s\.$""")
    private val USED_REGEX = Regex("""^§aYou used your §6(.+?) §aPickaxe Ability!\.""")

    var currentCooldown: Int = 0

    init {
        ClientReceiveMessageEvents.GAME.register(::onReceiveGameMessage)

        DevUtils.registerDrawTestText(10, 20, { "$currentCooldown" })
    }

    @Suppress("UNUSED_PARAMETER")
    fun onReceiveGameMessage(message: Text?, overlay: Boolean) {
        if (overlay) return
        val message = message?.string ?: return

        COOLDOWN_REGEX.matchEntire(message)?.let {
            currentCooldown = it.groups[1]?.value?.toIntOrNull() ?: return
            return
        }

        USED_REGEX.matchEntire(message)?.let {
            val abilityName = it.groups[1]?.value ?: return
            val ability = ABILITY_LIST.find { it.cuteName == abilityName } ?: return
            val selectedStack = MinecraftClient.getInstance().player!!.inventory.selectedStack
            val drillData = DrillTracker.extractData(selectedStack) ?: DrillTracker.PLACEHOLDER_DRILL
            val abilityLevel = 1 + drillData.hasBlueCheese.toInt() + /*TODO: HOTM*/ 1
            val baseCooldown = ability.cooldown[abilityLevel]
            val balModifier = if (PetTracker.isBal()) 0.9 else 1.0
            val fuelTankModifier = drillData.fuelTank.cooldownModifier
            currentCooldown = (baseCooldown * balModifier * fuelTankModifier).toInt()

            return
        }
    }
}
