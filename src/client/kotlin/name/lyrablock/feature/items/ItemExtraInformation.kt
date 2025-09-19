package name.lyrablock.feature.items

import name.lyrablock.util.math.NumberUtils.displayPrice
import net.minecraft.text.Text

/**
 * Utility for displaying item value information in tooltips.
 */
object ItemExtraInformation {
    /**
     * Returns extra value lines for the tooltip if the item is on Bazaar, Auction, or can be donated.
     */
    fun getExtraLines(
        id: String,
        count: Int,
        hasUuid: Boolean,
        onBazaar: Boolean,
        onAuction: Boolean,
        canDonate: Boolean
    ): List<Text>? =
        if (!(onBazaar || onAuction || canDonate)) null else buildList {
            if (onBazaar) {
                BazaarTracker.getStatus(id)?.let { status ->
                    add(Text.of("§eBazaar Buy: §6${displayPrice(status.buyPrice)}"))
                    add(Text.of("§eBazaar Sell: §6${displayPrice(status.sellPrice)}"))
                }
            }
        }
}
