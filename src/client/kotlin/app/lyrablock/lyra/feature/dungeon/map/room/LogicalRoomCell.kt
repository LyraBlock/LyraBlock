package app.lyrablock.lyra.feature.dungeon.map.room

/**
 * A disjoint set union (DSU) structure representing a room cell on the map.
 */
@Suppress("unused_parameter")
data class LogicalRoomCell(val rank: Int, val type: RoomType) {
    var parent: LogicalRoomCell = this
    val connections = mutableSetOf<LogicalRoomCell>()

    fun union(other: LogicalRoomCell) {
        // I don't think the `else` situation would happen but whatever.
        if (this.rank < other.rank) {
            this.parent = other
        } else {
            other.parent = this
        }
    }

    fun find(): LogicalRoomCell {
        if (this.parent != this) {
            this.parent = this.parent.find()
        }
        return this.parent
    }

    fun connect(other: LogicalRoomCell) {
        if (this.rank < other.rank) {
            this.connections.add(other)
        } else {
            other.connections.add(this)
        }
    }
}
