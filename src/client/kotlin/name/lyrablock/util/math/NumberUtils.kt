package name.lyrablock.util.math

import java.text.NumberFormat
import java.util.*


@Suppress("unused")
object NumberUtils {
    val US_NUMBER: NumberFormat = NumberFormat.getNumberInstance(Locale.US)

    fun parseLocaleInt(str: String) = US_NUMBER.parse(str).toInt()
    fun parseLocaleDouble(str: String) = US_NUMBER.parse(str).toDouble()
    fun toLocaleString(number: Number): String = US_NUMBER.format(number)!!
}
