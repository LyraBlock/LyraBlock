package app.lyrablock.lyra.feature.dungeon.map

import app.lyrablock.lyra.feature.dungeon.map.room.RoomData
import app.lyrablock.lyra.util.math.IntPoint

data class MapData(val rooms: List<RoomData>, val connections: Set<Pair<IntPoint, IntPoint>>)
