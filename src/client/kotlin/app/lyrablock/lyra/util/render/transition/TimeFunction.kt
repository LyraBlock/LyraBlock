package app.lyrablock.lyra.util.render.transition

import kotlin.math.pow

enum class TimeFunction(val invoke: (Double) -> Double) {
    LINEAR({ it }),
//    EASE_IN({ it * it }),
//    EASE_OUT({ it * (2 - it) }),
//    EXP_EASE_IN({ 2.0.pow(10 * (it - 1)) }),
//    EXP_EASE_OUT({ 1 - 2.0.pow(10 * (it - 1)) }),
    EXP_EASE_IN_OUT({ when {
       it < 0.5 -> 0.5 * 2.0.pow(10 * (2 * it - 1))
       else -> 0.5 * (2 - 2.0.pow(-10 * (2 * it - 1)))
    }}),
}
