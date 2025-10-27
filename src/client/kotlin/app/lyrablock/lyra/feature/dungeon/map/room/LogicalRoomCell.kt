package app.lyrablock.lyra.feature.dungeon.map.room

/**
 * A disjoint set union (DSU) structure representing a room cell on the map.
 */
class LogicalRoomCell(val rank: Int, val type: RoomColorType) {
    var parent: LogicalRoomCell = this
    val connections = mutableSetOf<LogicalRoomCell>()

    fun union(parent: LogicalRoomCell) {
        // I don't think the `else` situation would happen but whatever.
        if (this.rank < parent.rank) {
            this.parent = parent
        } else {
            parent.parent = this
        }
    }

    fun find(): LogicalRoomCell {
        if (this.parent != this) {
            this.parent = this.parent.find()
        }
        return this.parent
    }

    fun connect(that: LogicalRoomCell) {
        if (this.rank < that.rank) {
            this.connections.add(that)
        } else {
            that.connections.add(this)
        }
    }

    companion object {
        fun makeNulls(width: Int, height: Int) =
            Array(width) { arrayOfNulls<LogicalRoomCell>(height) }
    }
}
