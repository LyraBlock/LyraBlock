package app.lyrablock.lyra.feature.dungeon.map

import app.lyrablock.lyra.util.math.x
import app.lyrablock.lyra.util.math.y
import org.joml.Vector2dc
import org.joml.Vector2i
import org.joml.Vector2ic

/**
 * Math utils for dungeon map calculating
 *
 * Math!
 */
object DungeonMapMath {
    /**
     * Get the position of the northwest corner (that is, the point that has the least x and z) of the cell of the
     * current position.
     *
     * **Implement Note**. This is possible because the rooms are aligned to 32x32 grids, regardless of the size of
     * the map. However, this 32x32 baseline is shifted by 8x8. We shift 8x8 forehead and shift it back.
     *
     * @return The northwest corner position of the room in ints.
     */
    fun getPhysicalCellAnchor(x: Double, z: Double): Vector2ic {
        fun getAnchorComponent(t: Double) = (t + 8.5).toInt().floorDiv(32) * 32 + 8
        return Vector2i(getAnchorComponent(x), getAnchorComponent(z))
    }

    fun getPhysicalCellAnchor(pos: Vector2dc) = getPhysicalCellAnchor(pos.x, pos.y)
}
