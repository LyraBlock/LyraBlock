package app.lyrablock.lyra.event

import app.lyrablock.lyra.mixin.client.ClientPlayNetworkHandlerMixin
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.item.map.MapState
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket

@InvokedBy(ClientPlayNetworkHandlerMixin::class)
object MapEvents {
    fun interface MapUpdateApplied {
        fun onMapUpdateApplied(packet: MapUpdateS2CPacket, state: MapState)
    }

    @JvmField
    val MAP_UPDATE_APPLIED = EventFactory.createArrayBacked(MapUpdateApplied::class.java) { listeners ->
        MapUpdateApplied { packet, state ->
            listeners.forEach {
                it.onMapUpdateApplied(packet, state)
            }
        }
    }!!
}
