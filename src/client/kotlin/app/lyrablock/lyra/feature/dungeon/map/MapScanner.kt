package app.lyrablock.lyra.feature.dungeon.map

import app.lyrablock.lyra.feature.dungeon.map.room.LogicalRoomCell
import app.lyrablock.lyra.feature.dungeon.map.room.RoomType
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
        val pad6 = padding[6]!!
        val pad5 = padding[5]!!
        if ((coordinate - pad6) % STEP_SIZE == 0) return 6
        if ((coordinate - pad5) % STEP_SIZE == 0) return 5
        return 0
    }

    fun scanMap(rawData: ByteArray, xAxisSize: Int, yAxisSize: Int): Array<Array<LogicalRoomCell?>> {
        check(xAxisSize != 0 && yAxisSize != 0)

        fun colorAtPixel(x: Int, y: Int): RoomType? {
            if (x < 0 || y < 0 || x >= CANVAS_SIZE || y >= CANVAS_SIZE) return RoomType.TRANSPARENT
            val idx = x + y * CANVAS_SIZE
            if (idx >= rawData.size) return RoomType.TRANSPARENT
            return RoomType.fromColor(rawData[idx])
        }

        val cells = LogicalRoomCell.makeNulls(xAxisSize, yAxisSize)

        val paddingX = padding[xAxisSize]!!
        val paddingY = padding[yAxisSize]!!

        for (x in 0 until xAxisSize) for (y in 0 until yAxisSize) {

            val pixelX = x * STEP_SIZE + paddingX
            val pixelY = y * STEP_SIZE + paddingY
            val type = colorAtPixel(pixelX, pixelY) ?: continue
            val current = LogicalRoomCell(rank = y * xAxisSize + x, type)
            cells[y][x] = current

            val left = runCatching { cells[y][x - 1] }.getOrNull()
            val up = runCatching { cells[y - 1][x] }.getOrNull()

            // 1. Connections
            if (type == RoomType.TRANSPARENT) continue
            if (colorAtPixel(pixelX - 1, pixelY + 7) != RoomType.TRANSPARENT) {
                current.connect(left!!)
            }
            if (colorAtPixel(pixelX + 7, pixelY - 1) != RoomType.TRANSPARENT) {
                current.connect(up!!)
            }

            // 2. Room areas
            if (type != RoomType.REGULAR) continue

            // Regular rooms: check for merging with left or up regular rooms
            if (colorAtPixel(pixelX - 1, pixelY) == RoomType.REGULAR) {
                current.union(left!!)
                continue
            }

            if (colorAtPixel(pixelX, pixelY - 1) == RoomType.REGULAR) {
                current.union(up!!)
                continue
            }
        }

        return cells
    }
}
