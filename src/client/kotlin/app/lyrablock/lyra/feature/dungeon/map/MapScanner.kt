package app.lyrablock.lyra.feature.dungeon.map

import app.lyrablock.lyra.feature.dungeon.map.MapSpecification.Companion.CONNECTOR_SIZE
import app.lyrablock.lyra.feature.dungeon.map.room.LogicalRoomCell
import app.lyrablock.lyra.feature.dungeon.map.room.RoomColorType
import app.lyrablock.lyra.util.ArrayUtils.chunked

object MapScanner {
    val REGULAR_COLOR = RoomColorType.REGULAR.color

    /**
     * Scan the color list with the map specification, turning them into logical cells.
     *
     * **Note**. This function has a side effect that modifies the content of `data`.
     *
     * @param data The room data. **Its content will be modified**.
     */
    fun scanMap(data: Array<Array<LogicalRoomCell?>>, rawColors: ByteArray, specification: MapSpecification) {
        val cellSize = specification.cellSize
        val colors = rawColors.chunked(128)
        val (startingX, startingY) = specification.startingRoom
        val (maxI, maxJ) = data.size to data[0].size

        yIteration@ for (i in 1 until maxI) xIteration@ for (j in 1 until maxJ) {
            // Have you learned that Fencepost Problem?
            // (x, y) is the pixel position, (i, j) is the cell position on its grid.
            val x = startingX - CONNECTOR_SIZE + j * (cellSize + CONNECTOR_SIZE)
            val y = startingY - CONNECTOR_SIZE + i * (cellSize + CONNECTOR_SIZE)

            // Do not go too far
            if (y > 128) continue@yIteration
            if (x > 128) continue@xIteration
            if (data[i][j] != null) continue
            val color = colors[y][x]
            val type = RoomColorType.fromColor(color) ?: continue

            // Nearby cells. We assert they are not null, or here is a critical problem.
            val up = data[i][j - 1]!!
            val current = data[i][j]!!
            val left = data[i - 1][j]!!

            if (type != RoomColorType.REGULAR) {
                // All non-regular rooms is 1-cell wide.
                data[i][j] = LogicalRoomCell(j * maxJ + i, type)
            } else {
                // Try to merge regular rooms.
                if (colors[y][x - 1] == REGULAR_COLOR) {
                    current.union(left)
                }
                if (colors[y - 1][x] == REGULAR_COLOR)
                    current.union(up)
            }

            // Next, try to connect rooms.

            // How many pixels will we offset for searching?
            val seekDelta = cellSize / 2

            if (RoomColorType.fromColor(colors[y - 1][x + seekDelta]) != null
                && current.find() != up.find()
            ) {
                current.connect(up)
            }
            if (RoomColorType.fromColor(colors[y + seekDelta][x - 1]) != null
                && current.find() != left.find()
            ) {
                current.connect(left)
            }
        }
    }
}
