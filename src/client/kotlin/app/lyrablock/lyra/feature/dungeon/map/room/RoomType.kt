package app.lyrablock.lyra.feature.dungeon.map.room

enum class RoomType(val color: Byte = 0) {
    START(30),
    REGULAR,
    BLOOD,
    PUZZLE,
    FAIRY,
    TRAP,
    MINIBOSS,
    /** Marks an unlocked room, usually shown as a question mark on the map. */
    UNDISCOVERED,
    /** NOTHING */
    TRANSPARENT;

    companion object {
        private val colorToType = entries.reversed().associateBy { it.color }
        fun fromColor(color: Byte) = colorToType[color]
    }
}
