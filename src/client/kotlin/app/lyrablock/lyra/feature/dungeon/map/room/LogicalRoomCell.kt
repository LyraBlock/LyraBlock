package app.lyrablock.lyra.feature.dungeon.map.room

/**
 * A disjoint set union (DSU) structure representing a room cell on the map.
 */
@Suppress("unused_parameter")
data class LogicalRoomCell(val gridX: Int, val gridY: Int, val type: RoomType) {
    // why shl? it looks cuter
    private val rank = gridY shl 4 + gridX
    var parent: LogicalRoomCell = this
    val connections = mutableSetOf<LogicalRoomCell>()
    var roomId: String = ""
        get() = if (find() == this) field else find().roomId
        set(value) = if (find() == this) field = value else find().roomId = value

    /**
     * All elements that have the same parent.
     */
    val siblings = mutableSetOf(this)

    val shape: RoomShape by lazy {
        if (this != parent) parent.shape

        if (siblings.size == 1) RoomShape.CELL
        if (siblings.size == 2) RoomShape.R_1X2
        if (siblings.size == 3) {
            if (siblings.any { it.gridX - gridX == 2 || it.gridY - gridY == 2 }) RoomShape.R_1X3
            else RoomShape.L_SHAPED
        }
        if (siblings.size == 4) {
            if (siblings.any { it.gridX - gridX == 1 && it.gridY - gridY == 1 }) RoomShape.R_2X2
            else RoomShape.R_1X4
        }

        throw IllegalStateException("Room shape not found")
    }

    fun union(other: LogicalRoomCell) {
        // I don't think the `else` situation would happen but whatever.
        if (this.rank < other.rank) {
            this.parent = other
        } else {
            other.parent = this
        }
        other.siblings.addAll(this.siblings)
        this.siblings.addAll(other.siblings)
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
