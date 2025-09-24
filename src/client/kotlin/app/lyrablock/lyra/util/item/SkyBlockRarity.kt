package app.lyrablock.lyra.util.item

enum class SkyBlockRarity(val formatCode: String) {
    COMMON("§f"),
    UNCOMMON("§a"),
    RARE("§9"),
    EPIC("§5"),
    LEGENDARY("§6"),
    MYTHIC("§d"),
    DIVINE("§b"),
    ULTIMATE("§4"),
    SPECIAL("§c"),
    VERY_SPECIAL("§c");


    companion object {
        fun fromFormatCode(code: String): SkyBlockRarity? {
            assert(code != "§c") { "Cannot determine rarity by §c" }
            return entries.find { it.formatCode == code }
        }
    }
}
