package app.lyrablock.lyra.feature.mining

import app.lyrablock.lyra.LyraBlockClient
import app.lyrablock.lyra.LyraModule
import app.lyrablock.lyra.base.LyraTitleController
import app.lyrablock.lyra.feature.pet.PetTracker
import app.lyrablock.lyra.util.AbuseBoolean.toInt
import app.lyrablock.lyra.util.DevUtils
import app.lyrablock.lyra.util.item.ItemUtils
import app.lyrablock.lyra.util.render.PlaySoundHelper
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.network.chat.Component
import net.minecraft.server.MinecraftServer
import kotlin.math.ceil
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit


@LyraModule
object PickaxeAbilityCooldownTracker {
    private val COOLDOWN_REGEX = Regex("""^§cYour Pickaxe ability is on cooldown for ([0-9]+)s\.$""")
    private val USED_REGEX = Regex("""^§aYou used your §6(.+?) §aPickaxe Ability!""")

//    var ticksLeft: Int = 0
    val durationLeft get() = (totalDuration - (Clock.System.now() - startInstant)).coerceAtLeast(Duration.ZERO)
    val secondsLeft get() = ceil(durationLeft.toDouble(DurationUnit.SECONDS)).toInt()
//    var totalCooldown = 0
    var startInstant = Instant.DISTANT_FUTURE
    var activeAbility: PickaxeAbility? = null
    var totalDuration = 0.seconds
    var ready = false


    init {
        ClientReceiveMessageEvents.GAME.register(::onReceiveGameMessage)

        ServerTickEvents.END_SERVER_TICK.register(::onServerTick)

        DevUtils.registerDrawTestText(10, 20) { "$secondsLeft" }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onServerTick(server: MinecraftServer) {
        val now = Clock.System.now()
        if (now >= startInstant + totalDuration && !ready) {
            // java.lang.NullPointerException: null
            LyraTitleController.show(Component.nullToEmpty("${activeAbility?.abilityName} Ready!"))
            PlaySoundHelper.ding()
            ready = true
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onReceiveGameMessage(message: Component, overlay: Boolean) {
        if (overlay) return
        val message = message.string

        COOLDOWN_REGEX.matchEntire(message)?.let {
            val detectedSecondsLeft = it.groups[1]?.value?.toIntOrNull() ?: return
            if (detectedSecondsLeft != secondsLeft) {
                LyraBlockClient.LOGGER.error("Discrepancy detected in pickaxe ability cooldown! Local: $secondsLeft, Detected: $detectedSecondsLeft")
            }
            return
        }

        USED_REGEX.matchEntire(message)?.let { match ->
            val abilityName = match.groups[1]?.value ?: return
            activeAbility = PickaxeAbility.entries.find { it.abilityName == abilityName } ?: return
            val drillData = ItemUtils.selectedStack?.let { DrillTracker.extract(it) } ?: DrillTracker.PLACEHOLDER_DRILL
            val abilityLevel = 1 + drillData.hasBlueCheese.toInt() + /* TODO: HOTM */ 1

            val baseCooldown = activeAbility!!.cooldownList[abilityLevel]

            val balModifier = if (PetTracker.hasChimneyPerk()) 0.9 else 1.0
            val fuelTankModifier = drillData.fuelTank.cooldownModifier
            val skyMallModifier = 0.8
            totalDuration = baseCooldown.seconds * balModifier * fuelTankModifier * skyMallModifier
            startInstant = Clock.System.now()
            ready = false
            return
        }
    }
}
