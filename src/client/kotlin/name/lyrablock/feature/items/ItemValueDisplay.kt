package name.lyrablock.feature.items

import name.lyrablock.util.render.DrawContextDSL.withPushMatrix
import name.lyrablock.util.render.MatrixStackDSL.translate
import net.minecraft.client.font.TextRenderer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer
import net.minecraft.text.Text
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

/**
 * Utility for displaying item value information in tooltips.
 */
object ItemValueDisplay {
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

    /**
     * Formats a price with suffixes (k, M, B, T) and grouping separators.
     */
    private fun displayPrice(number: Double?, useUnitConversion: Boolean = true): String {
        if (number == null) return "null"
        if (useUnitConversion) {
            val absNumber = kotlin.math.abs(number)
            val suffixes = listOf(
                1_000_000_000_000.0 to "T",
                1_000_000_000.0 to "B",
                1_000_000.0 to "M",
                1_000.0 to "k"
            )
            suffixes.firstOrNull { absNumber >= it.first }?.let { (divisor, suffix) ->
                return String.format(Locale.US, "%.2f%s", absNumber / divisor, suffix)
            }
        }
        val symbols = DecimalFormatSymbols(Locale.US).apply { groupingSeparator = ',' }
        val pattern = if (kotlin.math.abs(number) >= 1000) "#,##0.00" else "#,##0.0"
        return DecimalFormat(pattern, symbols).format(number)
    }

    /**
     * Draws extra value lines below the tooltip.
     */
    fun drawExtra(
        context: DrawContext,
        x: Int,
        y: Int,
        width: Int,
        textRenderer: TextRenderer,
        text: List<Text>
    ) {
        val fontHeight = textRenderer.fontHeight
        TooltipBackgroundRenderer.render(context, x, y, width, fontHeight * text.size, 400, null)
        context.withPushMatrix {
            matrices.translate(x, y, 400)
            text.forEach {
                drawText(textRenderer, it, 0, 0, 0xffffff, false)
                matrices.translate(0, fontHeight)
            }
        }
    }
}
