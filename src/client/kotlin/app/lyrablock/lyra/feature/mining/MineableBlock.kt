package app.lyrablock.lyra.feature.mining

import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import kotlin.math.round

@Suppress("unused")
/**
 * All blocks that are affected by Skyblock's custom mining system.
 *
 * See [Block Strength - Hypixel SkyBlock Wiki](https://wiki.hypixel.net/Block_Strength) for more info.
 *
 * @param block Corresponding vanilla block
 * @param strength Block strength.
 * @param instaMine Minimum dig speed required for insta-mine.
 */
enum class MineableBlock(
    val type: MineableBlockType,
    val displayName: String,
    val block: Block,
    val strength: Int,
    val instaMine: Int,
) {
    STONE(MineableBlockType.BLOCK, "Stone", Blocks.STONE, 15, 450),
    HARD_STONE(MineableBlockType.BLOCK, "Hard Stone", Blocks.INFESTED_STONE, 50, 1500),
    COBBLESTONE(MineableBlockType.BLOCK, "Cobblestone", Blocks.COBBLESTONE, 20, 600),
    END_STONE(MineableBlockType.BLOCK, "End Stone", Blocks.END_STONE, 30, 1801),
    OBSIDIAN(MineableBlockType.BLOCK, "Obsidian", Blocks.OBSIDIAN, 500, 30001),
    NETHERRACK(MineableBlockType.BLOCK, "Netherrack", Blocks.NETHERRACK, 4, 241),
    ICE(MineableBlockType.BLOCK, "Ice", Blocks.ICE, 5, 301),
    COAL_ORE(MineableBlockType.ORE, "Coal", Blocks.COAL_ORE, 30, 1801),
    IRON_ORE(MineableBlockType.ORE, "Iron", Blocks.IRON_ORE, 30, 1801),
    GOLD_ORE(MineableBlockType.ORE, "Gold", Blocks.GOLD_ORE, 30, 1801),
    LAPIS_LAZULI_ORE(MineableBlockType.ORE, "Lapis", Blocks.LAPIS_ORE, 30, 1801),
    REDSTONE_ORE(MineableBlockType.ORE, "Redstone", Blocks.REDSTONE_ORE, 30, 1801),
    EMERALD_ORE(MineableBlockType.ORE, "Emerald", Blocks.EMERALD_ORE, 30, 1801),
    DIAMOND_ORE(MineableBlockType.ORE, "Diamond", Blocks.DIAMOND_ORE, 30, 1801),
    NETHER_QUARTZ_ORE(MineableBlockType.ORE, "Quartz", Blocks.NETHER_QUARTZ_ORE, 30, 1801),
    PURE_COAL(MineableBlockType.ORE, "Pure Coal", Blocks.COAL_BLOCK, 600, 36001),
    PURE_IRON(MineableBlockType.ORE, "Pure Iron", Blocks.IRON_BLOCK, 600, 36001),
    PURE_GOLD(MineableBlockType.ORE, "Pure Gold", Blocks.GOLD_BLOCK, 600, 36001),
    PURE_LAPIS(MineableBlockType.ORE, "Pure Lapis", Blocks.LAPIS_BLOCK, 600, 36001),
    PURE_REDSTONE(MineableBlockType.ORE, "Pure Redstone", Blocks.REDSTONE_BLOCK, 600, 36001),
    PURE_EMERALD(MineableBlockType.ORE, "Pure Emerald", Blocks.EMERALD_BLOCK, 600, 36001),
    PURE_DIAMOND(MineableBlockType.ORE, "Pure Diamond", Blocks.DIAMOND_BLOCK, 600, 36001),
    MITHRIL_GRAY_1(MineableBlockType.DWARVEN_METAL, "Mithril", Blocks.GRAY_WOOL, 500, 30001),
    MITHRIL_GRAY_2(MineableBlockType.DWARVEN_METAL, "Mithril", Blocks.CYAN_TERRACOTTA, 500, 30001),
    MITHRIL_PRISMARINE_1(MineableBlockType.DWARVEN_METAL, "Mithril", Blocks.PRISMARINE, 800, 48001),
    MITHRIL_PRISMARINE_2(MineableBlockType.DWARVEN_METAL, "Mithril", Blocks.DARK_PRISMARINE, 800, 48001),
    MITHRIL_PRISMARINE_3(MineableBlockType.DWARVEN_METAL, "Mithril", Blocks.PRISMARINE_BRICKS, 800, 48001),
    MITHRIL_BLUE(MineableBlockType.DWARVEN_METAL, "Mithril", Blocks.LIGHT_BLUE_WOOL, 1500, 90001),
    TITANIUM(MineableBlockType.DWARVEN_METAL, "Titanium", Blocks.DIORITE, 2000, 120001),
    RUBY_GEMSTONE_BLOCK(MineableBlockType.GEMSTONE, "Ruby", Blocks.RED_STAINED_GLASS, 2300, 138001),
    RUBY_GEMSTONE_PANE(MineableBlockType.GEMSTONE, "Ruby", Blocks.RED_STAINED_GLASS_PANE, 2300, 138001),
    AMBER_GEMSTONE_BLOCK(MineableBlockType.GEMSTONE, "Amber", Blocks.ORANGE_STAINED_GLASS, 3000, 180001),
    AMBER_GEMSTONE_PANE(MineableBlockType.GEMSTONE, "Amber", Blocks.ORANGE_STAINED_GLASS_PANE, 3000, 180001),
    AMETHYST_GEMSTONE_BLOCK(MineableBlockType.GEMSTONE, "Amethyst", Blocks.PURPLE_STAINED_GLASS, 3000, 180001),
    AMETHYST_GEMSTONE_PANE(MineableBlockType.GEMSTONE, "Amethyst", Blocks.PURPLE_STAINED_GLASS_PANE, 3000, 180001),
    JADE_GEMSTONE_BLOCK(MineableBlockType.GEMSTONE, "Jade", Blocks.LIME_STAINED_GLASS, 3000, 180001),
    JADE_GEMSTONE_PANE(MineableBlockType.GEMSTONE, "Jade", Blocks.LIME_STAINED_GLASS_PANE, 3000, 180001),
    SAPPHIRE_GEMSTONE_BLOCK(MineableBlockType.GEMSTONE, "Sapphire", Blocks.LIGHT_BLUE_STAINED_GLASS, 3000, 180001),
    SAPPHIRE_GEMSTONE_PANE(MineableBlockType.GEMSTONE, "Sapphire", Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, 3000, 180001),
    OPAL_GEMSTONE_BLOCK(MineableBlockType.GEMSTONE, "Opal", Blocks.WHITE_STAINED_GLASS, 3000, 180001),
    OPAL_GEMSTONE_PANE(MineableBlockType.GEMSTONE, "Opal", Blocks.WHITE_STAINED_GLASS_PANE, 3000, 180001),
    TOPAZ_GEMSTONE_BLOCK(MineableBlockType.GEMSTONE, "Topaz", Blocks.YELLOW_STAINED_GLASS, 3800, 228001),
    TOPAZ_GEMSTONE_PANE(MineableBlockType.GEMSTONE, "Topaz", Blocks.YELLOW_STAINED_GLASS_PANE, 3800, 228001),
    JASPER_GEMSTONE_BLOCK(MineableBlockType.GEMSTONE, "Jasper", Blocks.PINK_STAINED_GLASS, 4800, 288001),
    JASPER_GEMSTONE_PANE(MineableBlockType.GEMSTONE, "Jasper", Blocks.PINK_STAINED_GLASS_PANE, 4800, 288001),
    GLACITE(MineableBlockType.DWARVEN_METAL, "Glacite", Blocks.PACKED_ICE, 6000, 360001),
    UMBER_1(MineableBlockType.DWARVEN_METAL, "Umber", Blocks.TERRACOTTA, 5600, 336001),
    UMBER_2(MineableBlockType.DWARVEN_METAL, "Umber", Blocks.BROWN_TERRACOTTA, 5600, 336001,),
    UMBER_3(MineableBlockType.DWARVEN_METAL, "Umber", Blocks.RED_SANDSTONE_SLAB, 5600, 336001),
    TUNGSTEN_1(MineableBlockType.DWARVEN_METAL, "Tungsten", Blocks.INFESTED_COBBLESTONE, 5600, 336001),
    TUNGSTEN_2(MineableBlockType.DWARVEN_METAL, "Tungsten", Blocks.CLAY, 5600, 336001),
    ONYX_GEMSTONE_BLOCK(MineableBlockType.GEMSTONE, "Onyx", Blocks.BLACK_STAINED_GLASS, 5200, 312001),
    ONYX_GEMSTONE_PANE(MineableBlockType.GEMSTONE, "Onyx", Blocks.BLACK_STAINED_GLASS_PANE, 5200, 312001),
    AQUAMARINE_GEMSTONE_BLOCK(MineableBlockType.GEMSTONE, "Aquamarine", Blocks.BLUE_STAINED_GLASS, 5200, 312001),
    AQUAMARINE_GEMSTONE_PANE(MineableBlockType.GEMSTONE, "Aquamarine", Blocks.BLUE_STAINED_GLASS_PANE, 5200, 312001),
    CITRINE_GEMSTONE_BLOCK(MineableBlockType.GEMSTONE, "Citrine", Blocks.BROWN_STAINED_GLASS, 5200, 312001),
    CITRINE_GEMSTONE_PANE(MineableBlockType.GEMSTONE, "Citrine", Blocks.BROWN_STAINED_GLASS_PANE, 5200, 312001),
    PERIDOT_GEMSTONE_BLOCK(MineableBlockType.GEMSTONE, "Peridot", Blocks.GREEN_STAINED_GLASS, 5200, 312001),
    PERIDOT_GEMSTONE_PANE(MineableBlockType.GEMSTONE, "Peridot", Blocks.GREEN_STAINED_GLASS_PANE, 5200, 312001),
    SULPHUR(MineableBlockType.ORE, "Sulphur", Blocks.SPONGE, 500, 30001);

    /**
     * Get the time to mine this block with the given dig speed.
     *
     * @param digSpeed The dig speed, which is mining speed after several modifiers (namely Precision Mining buff, and
     * debuff in water / not on ground)
     *
     * @return Time to mine in ticks
     */
    fun timeToMine(digSpeed: Int): Int {
        if (digSpeed >= instaMine) return 0
        val time = round(30f * strength / digSpeed).toInt()
        // Softcap limits how fast a block can be mined to at least 4 ticks (or 0.2s).
        // The softcap can be bypassed by breaking a block instantly.
        return time.coerceAtLeast(4)
    }
}
