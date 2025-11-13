package app.lyrablock.lyra.event

import app.lyrablock.lyra.mixin.client.ClientPlayNetworkHandlerMixin
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket
import net.minecraft.world.level.saveddata.maps.MapItemSavedData

@InvokedBy(ClientPlayNetworkHandlerMixin::class)
object MapEvents {
    fun interface MapUpdateApplied {
        fun onMapUpdateApplied(packet: ClientboundMapItemDataPacket, state: MapItemSavedData)
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
