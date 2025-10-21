package app.lyrablock.lyra.feature.dungeon.map

import app.lyrablock.lyra.LyraModule
import app.lyrablock.lyra.event.MapEvents
import app.lyrablock.lyra.feature.dungeon.map.room.LogicalRoomCell
import app.lyrablock.lyra.feature.dungeon.map.room.RoomColorType
import app.lyrablock.lyra.util.SkyblockUtils
import app.lyrablock.lyra.util.math.IntRectangle
import app.lyrablock.lyra.util.math.IntSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.MapIdComponent
import net.minecraft.item.FilledMapItem
import net.minecraft.item.Items
import net.minecraft.item.map.MapState
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket
import kotlin.jvm.optionals.getOrNull

@LyraModule
object DungeonMapController {
    var mapId: MapIdComponent? = null
    var mapStartingRoom: IntRectangle? = null
    var physicalStartingRoom: IntRectangle? = null
    var mapSpec: IntSize? = null
    var roomCells: Array<Array<LogicalRoomCell?>>? = null

    val mapUpdateScope = CoroutineScope(Dispatchers.Default + Job())
    val mapUpdateMutex = Mutex()

    init {
        MapEvents.MAP_UPDATE_APPLIED.register(::onMapUpdate)
        ClientTickEvents.END_CLIENT_TICK.register(::fetchMapId)
        ClientTickEvents.END_CLIENT_TICK.register(::fetchStartingRoom)
    }

//    @OptIn(DelicateCoroutinesApi::class)
    @Suppress("unused_parameter")
    fun onMapUpdate(packet: MapUpdateS2CPacket, state: MapState) {
        if (packet.mapId != mapId) return
        packet.updateData.getOrNull() ?: return
        if (mapSpec == null) return

        mapUpdateScope.launch {
            mapUpdateMutex.withLock {
                roomCells = MapScanner.scanMap(state.colors, mapSpec!!.width, mapSpec!!.height)
            }
        }
    }

    fun fetchStartingRoom(client: MinecraftClient) {
        if (mapId == null) return
        if (mapSpec != null) return
        val state = FilledMapItem.getMapState(mapId, client.world) ?: return
        val colors = state.colors
        val start = colors.indexOf(RoomColorType.START.color)
        val last = colors.lastIndexOf(RoomColorType.START.color)
        mapSpec = IntSize(MapScanner.getAxisSize(start % 128), MapScanner.getAxisSize(start / 128))
        mapStartingRoom = IntRectangle.corners(MapScanner.toPoint(start), MapScanner.toPoint(last))
    }

    fun fetchMapId(client: MinecraftClient) {
        // If we're not in dungeons, ensure we don't keep a stale map id.
        if (!SkyblockUtils.isInDungeons()) {
            if (mapId != null) mapId = null
            return
        }

        // If we already found the map id, nothing to do.
        if (mapId != null) return

        val player = client.player ?: return
        val mapStack = player.inventory.getStack(8) ?: return

        if (mapStack.item != Items.FILLED_MAP) return
        val name = mapStack.name.string
        if (!name.contains("Magical Map")) return

        // Obtain and record the map id component.
        val foundMapId = mapStack.get(DataComponentTypes.MAP_ID) ?: return
        mapId = foundMapId
    }
}
