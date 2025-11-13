package app.lyrablock.lyra.feature.items

import app.lyrablock.lyra.util.math.NumberUtils.displayPrice
import net.minecraft.network.chat.Component

/**
 * Utility for displaying item value information in tooltips.
 */
object ItemExtraInformation {
    /**
     * Returns extra value lines for the tooltip if the item is on Bazaar, Auction, or can be donated.
     */
    fun of(
        id: String,
        count: Int,
        hasUuid: Boolean,
        onBazaar: Boolean,
        onAuction: Boolean,
        canDonate: Boolean
    ): List<Component>? =
        if (!(onBazaar || onAuction || canDonate)) null else buildList {
            if (onBazaar) {
                BazaarTracker.getStatus(id)?.let { status ->
                    add(Component.nullToEmpty("§eBazaar Buy: §6${displayPrice(status.buyPrice)}"))
                    add(Component.nullToEmpty("§eBazaar Sell: §6${displayPrice(status.sellPrice)}"))
                }
            }
        }
}
