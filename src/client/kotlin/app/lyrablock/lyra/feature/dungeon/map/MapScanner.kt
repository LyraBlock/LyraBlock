package app.lyrablock.lyra.feature.dungeon.map

import app.lyrablock.lyra.feature.dungeon.map.room.RoomData
import app.lyrablock.lyra.feature.dungeon.map.room.RoomType
import app.lyrablock.lyra.util.math.IntPoint
import app.lyrablock.lyra.util.math.IntVec2

object MapScanner {
    const val CANVAS_SIZE = 128
    const val ROOM_SIZE = 16
    const val CONNECTOR_SIZE = 4
    const val STEP_SIZE = CONNECTOR_SIZE + ROOM_SIZE

    fun toPoint(index: Int) = IntVec2.of((index / CANVAS_SIZE) to (index % CANVAS_SIZE))


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

    fun scanMap(rawData: ByteArray, xAxisSize: Int, yAxisSize: Int): MapData {
        check(xAxisSize != 0 && yAxisSize != 0)

        fun colorAtPixel(x: Int, y: Int): RoomType? {
            if (x < 0 || y < 0 || x >= CANVAS_SIZE || y >= CANVAS_SIZE) return RoomType.TRANSPARENT
            val idx = x + y * CANVAS_SIZE
            if (idx >= rawData.size) return RoomType.TRANSPARENT
            return RoomType.fromColor(rawData[idx])
        }

        val connections = mutableSetOf<Pair<IntPoint, IntPoint>>()
        val roomData = mutableMapOf<IntPoint, RoomData>()

        val paddingX = padding[xAxisSize]!!
        val paddingY = padding[yAxisSize]!!

        for (x in 0 until xAxisSize) for (y in 0 until yAxisSize) {
            val thisPoint = IntPoint(x, y)
            val leftPoint = IntPoint(x - 1, y)
            val upPoint = IntPoint(x, y - 1)

            val pixelX = x * STEP_SIZE + paddingX
            val pixelY = y * STEP_SIZE + paddingY
            val type = colorAtPixel(pixelX, pixelY) ?: continue

            // 1. Connections
            if (type == RoomType.TRANSPARENT) continue
            if (colorAtPixel(pixelX - 1, pixelY + 7) != RoomType.TRANSPARENT) {
                connections.add(thisPoint to leftPoint)
            }
            if (colorAtPixel(pixelX + 7, pixelY - 1) != RoomType.TRANSPARENT) {
                connections.add(thisPoint to upPoint)
            }

            // 2. Room areas
            if (type != RoomType.REGULAR) {
                roomData[thisPoint] = RoomData(type, mutableSetOf(thisPoint))
                continue
            }

            // Regular rooms: check for merging with left or top regular rooms
            if (colorAtPixel(pixelX - 1, pixelY) == RoomType.REGULAR) {
                roomData[leftPoint]?.area?.add(thisPoint)
                continue
            }

            if (colorAtPixel(pixelX, pixelY - 1) == RoomType.REGULAR) {
                roomData[upPoint]?.area?.add(thisPoint)
                continue
            }

            roomData[thisPoint] = RoomData(RoomType.REGULAR, mutableSetOf(thisPoint))
        }

        return MapData(roomData.values.toList(), connections.toSet())
    }
}
