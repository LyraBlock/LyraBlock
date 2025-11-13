package app.lyrablock.lyra.feature.pet

import app.lyrablock.lyra.event.ScreenHandlerEvents
import app.lyrablock.lyra.util.item.SkyBlockRarity
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.ClickType

// TODO: this should be profile related.
object PetTracker {
    data class PetData(var name: String, var rarity: SkyBlockRarity)

    val activePet = PetData("Bal", SkyBlockRarity.COMMON)

    init {
        ScreenHandlerEvents.SLOT_CLICK.register(::onSlotClick)
        ClientReceiveMessageEvents.GAME.register(::onReceiveMessage)
    }

    val SUMMON_PET_PATTERN = Regex("""^§aYou summoned your (§.)([A-Za-z]+?)( §.✦)?!$""")

    val PET_NAME_PATTERN = Regex("""^(. )?§7\[Lvl (\d+)] (§.)([A-Z a-z]+)""")

    fun onSlotClick(slotIndex: Int, button: Int, actionType: ClickType, player: Player) {
        if (button == 0) {
            val stack = player.containerMenu.getSlot(slotIndex).item
            val name = stack.customName?.string ?: return

            PET_NAME_PATTERN.matchEntire(name)?.let { match ->
                val rarityFormatCode = match.groups[3]?.value ?: return
                val petName = match.groups[4]?.value ?: return
                activePet.name = petName
                activePet.rarity = SkyBlockRarity.fromFormatCode(rarityFormatCode)!!
            }
        }
    }

    fun onReceiveMessage(message: Component, overlay: Boolean) {
        if (overlay) return
        val message = message.string

        SUMMON_PET_PATTERN.matchEntire(message)?.let { match ->
            val petName = match.groups[2]?.value ?: return
            activePet.name = petName
            val rarityFormatCode = match.groups[1]?.value ?: return
            activePet.rarity = SkyBlockRarity.fromFormatCode(rarityFormatCode) ?: return
            return
        }

    }

    // Mystical Ritual
    fun isGriffin() = true

    /**
     * LEGENDARY Bal
     * Chimney: Reduce Pickaxe Ability cooldowns by 10%.
     */
    fun hasChimneyPerk() = activePet.name == "Bal" && activePet.rarity >= SkyBlockRarity.LEGENDARY

    /**
     * RARE Snail
     * Slow and Steady: Convert every 3 ✦Speed you have above 100 into +1 ☘Block Fortune.
     */
    fun hasSlowAndSteady() = activePet.name == "Snail" && activePet.rarity >= SkyBlockRarity.RARE
}
