package app.lyrablock.lyra.feature.dungeon.map.room

enum class RoomType(val color: Byte = 0) {
    START(30),
    REGULAR(63),
    BLOOD(18),
    PUZZLE(66),
    FAIRY(82),
    TRAP(62),
    MINIBOSS(74),
    /** Marks an unlocked room, usually shown as a question mark on the map. */
    UNDISCOVERED(85);

    companion object {
        private val colorToType = entries.reversed().associateBy { it.color }
        fun fromColor(color: Byte) : RoomType? = colorToType[color]
    }
}
