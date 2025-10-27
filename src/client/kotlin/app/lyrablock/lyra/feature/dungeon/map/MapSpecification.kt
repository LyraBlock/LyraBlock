package app.lyrablock.lyra.feature.dungeon.map

import app.lyrablock.lyra.LyraBlockClient
import app.lyrablock.lyra.feature.dungeon.map.room.RoomType
import net.minecraft.item.map.MapState
import kotlin.time.measureTimedValue

/**
 * Describe the Magical Map specification.
 *
 * @param startingRoom The left-top pixel index of the starting room.
 * @param cellSize The size of each cell (1x1 room).
 */
data class MapSpecification(val startingRoom: Pair<Int, Int>, val cellSize: Int) {
    /**
     * The starting room index (in the `colors` array). Not sure if this is gonna used, whatsoever.
     */
    val startingRoomIndex by lazy {
        val (x, y) = startingRoom
        y * 128 + x
    }

    /**
     * Assuming the map is filled as much as possible (i.e., the padding of the map cannot fit any more cell), the
     * top-left room.
     *
     * We do not actually care if the room is not filled, but treating so will be convenient.
     *
     * **Implementation Note**: The connector is always 4-pixel wide on the main axis, 5-pixel wide on the cross axis.
     */
    val topLeftRoom = run {
        val (x, y) = startingRoom
        x.mod(cellSize + 4) to y.mod(cellSize + 4)
    }

    /**
     * The max cells on each axis. This is used for traversal, and we will want to test if the index is out of range
     * there. So here it is okay to be a bit greater than the actual value.
     */
    val maxCells = 128 / (cellSize + 4)

    companion object {
        const val CONNECTOR_SIZE = 4

        fun fromColors(colors: ByteArray): MapSpecification {
            val (result, duration) = measureTimedValue {
                val first = colors.indexOf(RoomType.START.color)
                val last = colors.lastIndexOf(RoomType.START.color)
                val firstX = first % 128
                val firstY = first / 128
                val height = (last / 128) - firstY + 1

                MapSpecification(firstX to firstY, height)
            }
            LyraBlockClient.LOGGER.info("Getting room spec took ${duration.inWholeMilliseconds}ms.")
            return result
        }

        fun fromMapState(state: MapState): MapSpecification = fromColors(state.colors)
    }
}
