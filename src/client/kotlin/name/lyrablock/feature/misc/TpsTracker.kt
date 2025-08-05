package name.lyrablock.feature.misc

import kotlinx.datetime.Clock
import name.lyrablock.InitializeWithClient
import name.lyrablock.LyraSubCommandRegister
import name.lyrablock.util.ChatSender
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.util.Identifier

@InitializeWithClient
object TpsTracker {

    // The samples used in calculation.
    const val TICK_SAMPLES = 100
    val tickData = mutableListOf<Long>()

    init {
        ServerTickEvents.END_SERVER_TICK.register(::onServerTick)
        ServerPlayConnectionEvents.JOIN.register(::onJoinServer)
        // This is a temporary solution. This should be migrated to `Widget` in the future.
        // 3-5 business days.
        HudLayerRegistrationCallback.EVENT.register {
            it.attachLayerAfter(
                IdentifiedLayer.MISC_OVERLAYS, Identifier.of("lyra:tps_test")
            ) { context, tickDeltaManager ->
                val tps = getTps()
                context.drawText(
                    MinecraftClient.getInstance().textRenderer,
                    if (tps == null) "TPS: §7(Collecting...)" else "TPS: " + getTpsDisplay(),
                    10,
                    10,
                    0xFFFFFFFF.toInt(),
                    true
                )
            }
        }

        ClientCommandRegistrationCallback.EVENT.register { dispatcher, registryAccess ->
            LyraSubCommandRegister.register(dispatcher, "tps") {
                val tps = getTps()
                if (tps == null) {
                    ChatSender.sendInfo("TPS: §cUnknown yet")
                } else {
                    ChatSender.sendInfo("TPS: ${getTpsDisplay()}")
                }
                return@register 1
            }
        }
    }

    fun onServerTick(server: MinecraftServer) {
        tickData.add(Clock.System.now().toEpochMilliseconds())
        if (tickData.size > TICK_SAMPLES) {
            tickData.removeFirst()
        }
    }

    fun onJoinServer(handler: ServerPlayNetworkHandler, sender: PacketSender, server: MinecraftServer) {
        tickData.clear()
    }

    fun getTps(): Float? {
        if (tickData.size < TICK_SAMPLES) return null

        // Note this is not the so-called *mspt*.
        val averageMillisecondsBetweenTicks = ((tickData.last() - tickData.first()) / TICK_SAMPLES).toInt()

        if (averageMillisecondsBetweenTicks == 0) return null
        return 1000f / averageMillisecondsBetweenTicks
    }

    fun getTpsColor(tps: Float): String = when {
        // These critical values are provided by Skyhanni
        tps >= 19.8f -> "§2"
        tps >= 19.0f -> "§a"
        tps >= 17.5f -> "§6"
        tps >= 12.0f -> "§c"
        else -> "§4"
    }

    fun getTpsDisplay(): String? {
        val tps = getTps() ?: return null
        val color = getTpsColor(tps)
        return color + "%.1f".format(tps)
    }
}
