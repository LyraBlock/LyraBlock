package app.lyrablock.lyra.feature.dungeon.map.room

import app.lyrablock.lyra.util.math.IntPoint

data class RoomData(
    val type: RoomType,
    val area: MutableSet<IntPoint>,
    val connections: MutableSet<IntPoint> = mutableSetOf()
)
