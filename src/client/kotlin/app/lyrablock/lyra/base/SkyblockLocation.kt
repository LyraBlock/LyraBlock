package app.lyrablock.lyra.base

import org.jetbrains.annotations.Contract

// TODO: Should we make this repo-based?
enum class SkyblockLocation(val id: String, val friendlyName: String) {
    PRIVATE_ISLAND("dynamic", "Private Island"),
    GARDEN("garden", "Garden"),
    HUB("hub", "Hub"),
    THE_FARMING_ISLANDS("farming_1", "The Farming Island"),
    THE_PARK("foraging_1", "The Park"),
    SPIDERS_DEN("combat_1", "Spider's Den"),
    THE_END("combat_3", "The End"),
    CRIMSON_ISLE("crimson_isle", "Crimson Isle"),
    GOLD_MINE("mining_1", "Gold Mine"),
    DEEP_CAVERNS("mining_2", "Deep Caverns"),
    DWARVEN_MINES("mining_3", "Dwarven Mines"),
    BACKWATER_BAYOU("fishing_1", "Backwater Bayou"),
    DUNGEON_HUB("dungeon_hub", "Dungeon Hub"),
    WINTER_ISLAND("winter", "Jerry's Workshop"),
    THE_RIFT("rift", "The Rift"),
    DARK_AUCTION("dark_auction", "Dark Auction"),
    CRYSTAL_HOLLOWS("crystal_hollows", "Crystal Hollows"),
    DUNGEON("dungeon", "Dungeons"),
    KUUDRA_HOLLOW("kuudra", "Kuudra's Hollow"),
    GLACITE_MINESHAFT("mineshaft", "Glacite Mineshaft"),
    GALATEA("foraging_2", "Galatea");

    companion object {
        val idMap = entries.associateBy { it.id }
        val friendlyNameMap = entries.associateBy { it.friendlyName }

        @Contract("null -> null, _ -> _")
        fun fromId(id: String?) = idMap[id]
    }
}
