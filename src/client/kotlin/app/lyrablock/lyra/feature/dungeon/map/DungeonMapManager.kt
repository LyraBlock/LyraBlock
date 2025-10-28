package app.lyrablock.lyra.feature.dungeon.map

import app.lyrablock.lyra.LyraModule
import app.lyrablock.lyra.base.HypixelInfo
import app.lyrablock.lyra.event.MapEvents
import app.lyrablock.lyra.feature.dungeon.map.room.PhysicalRoomCell
import app.lyrablock.lyra.util.math.takeXZ
import app.lyrablock.lyra.util.math.toVector3dc
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld
import net.minecraft.component.DataComponentTypes
import net.minecraft.component.type.MapIdComponent
import net.minecraft.entity.decoration.ArmorStandEntity
import net.minecraft.item.FilledMapItem
import net.minecraft.item.Items
import net.minecraft.item.map.MapState
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket
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
            mapSpec = null
            mapData = null
        }
    }

    var physicalStartingRoom: PhysicalRoomCell? = null
    var mapId: MapIdComponent? = null
    var mapSpec: MapSpecification? = null
    var mapData: MapData? = null
    var currentPhysical: PhysicalRoomCell? = null
    var currentLogicalIndex: Pair<Int, Int>? = null

    val scanMapScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    val scanMapMutex = Mutex()

    fun onTick(client: MinecraftClient) {
        if (!HypixelInfo.isInDungeons) return
        val world = client.world ?: return
        val player = client.player ?: return

        if (physicalStartingRoom == null)
            findMortPos(world)?.let { physicalStartingRoom = PhysicalRoomCell.at(it.takeXZ()) }

        if (mapId == null) mapId = getMapId(player)

        if (mapSpec == null)
            mapSpec = FilledMapItem.getMapState(mapId, world)?.let { MapSpecification.fromMapState(it) }

        if (mapData == null && mapSpec != null) {
            val maxCells = mapSpec!!.maxCells
            mapData = MapData(maxCells, maxCells)
            FilledMapItem.getMapState(mapId, world)?.let { state ->
                scanMapScope.launch {
                    scanMapMutex.withLock {
                        mapData?.scanMap(state.colors, mapSpec!!)
                    }
                }
            }
        }

        // From here on, we will no longer modify the values of these variables.
        // We store them as `val`s so that they are also ensured not to be modified concurrently,
        // making Kotlin's automatic (non-null) casting properly work.
        val mapData = mapData ?: return
        val mapSpec = mapSpec ?: return
        val physicalStartingRoom = physicalStartingRoom ?: return

        val currentPhysical = PhysicalRoomCell.at(player.pos.toVector3dc().takeXZ())
        this.currentPhysical = currentPhysical
        val currentLogicalIndex = currentPhysical.toLogicalIndex(mapSpec, physicalStartingRoom)
        this.currentLogicalIndex = currentLogicalIndex

    }

    fun onMapUpdate(packet: MapUpdateS2CPacket, state: MapState) {
        if (packet.mapId != mapId || mapId == null) return
        if (mapData == null) return
        scanMapScope.launch {
            scanMapMutex.withLock {
                mapData!!.scanMap(state.colors, mapSpec!!)
            }
        }
    }

    /**
     * Fetch the **name tag** coordinates of Mort.
     */
    private fun findMortPos(world: ClientWorld): Vector3dc? {
        return world.entities.find { it is ArmorStandEntity && it.name.string.contains("Mort") }?.pos?.toVector3dc()
    }

    private fun getMapId(player: ClientPlayerEntity): MapIdComponent? {
        val mapStack = player.inventory.getStack(8)
        if (mapStack.item != Items.FILLED_MAP) return null
        val name = mapStack.name.string
        // Replace this with a repo pattern?
        if (!name.contains("Magical Map")) return null
        return mapStack.get(DataComponentTypes.MAP_ID)
    }
}
