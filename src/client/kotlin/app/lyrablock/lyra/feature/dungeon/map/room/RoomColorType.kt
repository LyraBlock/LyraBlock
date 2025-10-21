package app.lyrablock.lyra.feature.dungeon.map.room

import net.minecraft.block.MapColor

enum class RoomColorType(val color: Byte = 0) {
    START(MapColor.DARK_GREEN),
    REGULAR(MapColor.BROWN),
    BLOOD(MapColor.RED),
    PUZZLE(MapColor.PURPLE),
    FAIRY(MapColor.PINK),
    TRAP(MapColor.ORANGE),
    MINIBOSS(MapColor.YELLOW),
    /** Marks an unlocked room, usually shown as a question mark on the map. */
    UNDISCOVERED(MapColor.GRAY);

    constructor(color: MapColor) : this(color.id.toByte())

    companion object {
        private val colorToType = entries.reversed().associateBy { it.color }
        fun fromColor(color: Byte) : RoomColorType? = colorToType[color]
    }
}
