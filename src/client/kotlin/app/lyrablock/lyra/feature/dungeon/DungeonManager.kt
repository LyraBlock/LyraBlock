package app.lyrablock.lyra.feature.dungeon

import app.lyrablock.lyra.LyraModule
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientWorldEvents
import net.minecraft.client.MinecraftClient

/**
 * The center controller for dungeons.
 */
@LyraModule
object DungeonManager {
    init {
        ClientTickEvents.START_CLIENT_TICK.register(::tick)
        ClientWorldEvents.AFTER_CLIENT_WORLD_CHANGE.register { _, _ -> invalidate() }
    }

//    var startingRoomPos: Vector2ic? = null
//    var mapId: MapIdComponent? = null
//    var mapSpec: MapSpecification? = null
//    var mapData = null

    private fun tick(client: MinecraftClient) {

    }

    /**
     * Invalidate all stored data.
     */
    fun invalidate() {
//        startingRoomPos = null
//        mapSpec = null
//        mapId = null
    }
}
