package app.lyrablock.lyra.feature.dungeon.map

import app.lyrablock.lyra.feature.dungeon.map.room.LogicalRoomCell
import app.lyrablock.lyra.feature.dungeon.map.room.RoomColorType
import app.lyrablock.lyra.util.ArrayUtils.chunked
import app.lyrablock.lyra.util.math.IntPoint

object MapScanner {
    const val CANVAS_SIZE = 128
    const val ROOM_SIZE = 16
    const val CONNECTOR_SIZE = 4
    const val STEP_SIZE = CONNECTOR_SIZE + ROOM_SIZE

    fun toPoint(index: Int) = IntPoint((index % CANVAS_SIZE), (index / CANVAS_SIZE))


    // 6-types have 5-pixel padding on the left, and
    // 5-types have 18-pixel padding on the left.
    // Map keys are axis sizes -> left padding (connector compensated)
    val padding = mapOf(6 to 5 - CONNECTOR_SIZE, 5 to 18 - CONNECTOR_SIZE)

    /**
     * A dungeon map is one of 4x4, 5x5 (sometimes, a puzzle room takes a row/column, making it 5x6), or 6x6.
     * Here, we consider 4x4 the same as 6x6. They differ little in our processing.
     *
     * Because we know that the content of Magical Map is always centered, we can tell if the rooms in an axis
     * (horizon or vertical) is 5 or 6 by checking the coordinate on the axis, of the top-left most pixel,
     * of one room.
     *
     * @return The size of the axis, 5 or 6. If the coordinate is invalid, return 0.
     */
    fun getAxisSize(coordinate: Int): Int {
        return padding.toList().firstOrNull { (_, pad) -> (coordinate - pad) % STEP_SIZE == 0 }?.first ?: 0
    }

    fun scanMap(rawColors: ByteArray, xAxisSize: Int, yAxisSize: Int): Array<Array<LogicalRoomCell?>> {
        check(xAxisSize != 0 && yAxisSize != 0)
        val colors = rawColors.chunked(CANVAS_SIZE)

        val cells = LogicalRoomCell.makeNulls(xAxisSize, yAxisSize)

        val paddingX = padding[xAxisSize]!!
        val paddingY = padding[yAxisSize]!!

        for (cellX in 0 until xAxisSize) for (cellY in 0 until yAxisSize) {

            val x = cellX * STEP_SIZE + paddingX
            val y = cellY * STEP_SIZE + paddingY
            val type = colors.getOrNull(y)?.getOrNull(x)?.let { RoomColorType.fromColor(it) } ?: continue
            val current = LogicalRoomCell(rank = cellY * xAxisSize + cellX, type)
            cells[cellY][cellX] = current

            val left = cells.getOrNull(cellY)?.getOrNull(cellX - 1)
            val up = cells.getOrNull(cellY - 1)?.getOrNull(cellX)

            // 1. Connections
            if (RoomColorType.fromColor(colors[y + 7][x - 1]) != null)
                current.connect(left!!)

            if (RoomColorType.fromColor(colors[y - 1][x + 7]) != null)
                current.connect(up!!)


            // 2. Merging regular rooms
            if (type != RoomColorType.REGULAR) continue

            // Regular rooms: check for merging with left or up regular rooms
            if (colors[y][x - 1] == RoomColorType.REGULAR.color) {
                current.union(left!!)
                continue
            }

            if (colors[y - 1][x] == RoomColorType.REGULAR.color) {
                current.union(up!!)
                continue
            }
        }

        return cells
    }
}
