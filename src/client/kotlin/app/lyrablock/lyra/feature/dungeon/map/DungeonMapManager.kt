package app.lyrablock.lyra.feature.dungeon.map

import app.lyrablock.lyra.LyraModule
import app.lyrablock.lyra.base.HypixelInfo
import app.lyrablock.lyra.event.MapEvents
import app.lyrablock.lyra.feature.dungeon.map.room.PhysicalRoomCell
import app.lyrablock.lyra.util.math.takeXZ
import app.lyrablock.lyra.util.math.toVector3dc
import app.lyrablock.lyra.util.position
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.component.DataComponents
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket
import net.minecraft.world.entity.decoration.ArmorStand
import net.minecraft.world.item.Items
import net.minecraft.world.item.MapItem
import net.minecraft.world.level.saveddata.maps.MapId
import net.minecraft.world.level.saveddata.maps.MapItemSavedData
import org.joml.Vector3dc

@LyraModule
@Suppress("unused")
object DungeonMapManager {
    init {
        ClientTickEvents.END_CLIENT_TICK.register(::onTick)
        MapEvents.MAP_UPDATE_APPLIED.register(::onMapUpdate)
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register { _, _ ->
            physicalStartingRoom = null
            mapId = null
            mapData = null
        }
    }

    var physicalStartingRoom: PhysicalRoomCell? = null
    var mapId: MapId? = null
    var mapData: MapData? = null
    var currentPhysical: PhysicalRoomCell? = null
    var currentLogicalIndex: Pair<Int, Int>? = null

    val scanMapScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val scanMapMutex = Mutex()

    fun onTick(client: Minecraft) {
        if (!HypixelInfo.isInDungeons) return
        val world = client.level ?: return
        val player = client.player ?: return

        if (physicalStartingRoom == null)
            findMortPos(world)?.let { physicalStartingRoom = PhysicalRoomCell.at(it.takeXZ()) }

        if (mapId == null) mapId = getMapId(player)

        if (mapData == null) {
            val state = MapItem.getSavedData(mapId, world)
            // Get map specification, then pass it to mapData
            val spec = state?.let { MapSpecification.fromMapState(it) }
            if (spec != null) {
                val maxCells = spec.maxCells
                mapData = MapData(maxCells, maxCells, spec)
                scanMapScope.launch {
                    scanMapMutex.withLock {
                        mapData?.scanMap(state.colors)
                    }
                }
            }
        }
        // From here on, we will no longer modify the values of these variables.
        // We store them as `val`s so that they are also ensured not to be modified concurrently,
        // making Kotlin's automatic (non-null) casting properly work.
        val mapData = mapData ?: return
        val mapSpec = mapData.specification
        val physicalStartingRoom = physicalStartingRoom ?: return

        val currentPhysical = PhysicalRoomCell.at(player.position.toVector3dc().takeXZ())
        this.currentPhysical = currentPhysical
        val currentLogicalIndex = currentPhysical.toLogicalIndex(mapSpec, physicalStartingRoom)
        this.currentLogicalIndex = currentLogicalIndex

    }

    fun onMapUpdate(packet: ClientboundMapItemDataPacket, state: MapItemSavedData) {
        if (packet.mapId != mapId || mapId == null) return
        if (mapData == null) return
        scanMapScope.launch {
            scanMapMutex.withLock {
                mapData!!.scanMap(state.colors)
            }
        }
    }

    /**
     * Fetch the **name tag** coordinates of Mort.
     */
    private fun findMortPos(world: ClientLevel): Vector3dc? {
        return world.entitiesForRendering().find { it is ArmorStand && it.name.string.contains("Mort") }?.position?.toVector3dc()
    }

    private fun getMapId(player: LocalPlayer): MapId? {
        val mapStack = player.inventory.getItem(8)
        if (mapStack.item != Items.FILLED_MAP) return null
        val name = mapStack.hoverName.string
        // Replace this with a repo pattern?
        if (!name.contains("Magical Map")) return null
        return mapStack.get(DataComponents.MAP_ID)
    }
}
