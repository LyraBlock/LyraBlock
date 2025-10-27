package app.lyrablock.lyra.feature.dungeon.map.room

import app.lyrablock.lyra.feature.dungeon.map.MapSpecification
import app.lyrablock.lyra.feature.dungeon.map.room.PhysicalRoomCell.Companion.at
import app.lyrablock.lyra.feature.dungeon.map.room.PhysicalRoomCell.Companion.getComponentAnchor
import app.lyrablock.lyra.util.math.x
import app.lyrablock.lyra.util.math.y
import org.joml.*

/**
 * The physical (in the world) room cell.
 *
 * The *anchor* here means the corner that has the least x and z (that is, northwest corner).
 */
data class PhysicalRoomCell(val anchorX: Int, val anchorZ: Int) {
    fun toVector(): Vector2ic = Vector2i(anchorX, anchorZ)

    /**
     * Transform this physical room to a logical room.
     */
    fun toLogical(data: Array<Array<LogicalRoomCell?>>,  spec: MapSpecification, physicalEntrance: PhysicalRoomCell): LogicalRoomCell? {
        val cellSize = spec.cellSize
        val (j, i) = (this.toVector() - physicalEntrance.toVector()) / 32 * (cellSize + MapSpecification.CONNECTOR_SIZE)
        return data[i][j]
    }

    companion object {
        /**
         * Get the position of the northwest corner, for each coordinate component.
         *
         * @see at
         */
        private fun getComponentAnchor(t: Double) = (t + 8.5).toInt().floorDiv(32) * 32 + 8

        /**
         * Get the position of the northwest corner of the cell of the
         * current position.
         *
         * **Implement Note**. This is possible because the rooms are aligned to 32x32 grids, regardless of the size of
         * the map. However, this 32x32 baseline is shifted by 8x8. We shift 8x8 forehead and shift it back.
         *
         * @return The northwest corner position of the room in ints.
         * @see getComponentAnchor
         */
        fun at(worldX: Double, worldZ: Double) =
            PhysicalRoomCell(getComponentAnchor(worldX), getComponentAnchor(worldZ))

        fun at(worldPos: Vector2dc) = at(worldPos.x, worldPos.y)
    }
}
