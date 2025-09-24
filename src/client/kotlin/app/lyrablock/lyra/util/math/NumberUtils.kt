package app.lyrablock.lyra.util.math

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.*
import kotlin.math.abs


@Suppress("unused")
object NumberUtils {
    val US_NUMBER: NumberFormat = NumberFormat.getNumberInstance(Locale.US)

    fun parseLocaleInt(str: String) = US_NUMBER.parse(str).toInt()
    fun parseLocaleDouble(str: String) = US_NUMBER.parse(str).toDouble()
    fun toLocaleString(number: Number): String = US_NUMBER.format(number)!!


    /**
     * Formats a price with suffixes (k, M, B, T) and grouping separators.
     */
    fun displayPrice(number: Double?, useUnitConversion: Boolean = true): String {
        if (number == null || number.isNaN()) return "null"
        if (useUnitConversion) {
            val absNumber = abs(number)
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
        val pattern = if (abs(number) >= 1000) "#,##0.00" else "#,##0.0"
        return DecimalFormat(pattern, symbols).format(number)
    }
}
