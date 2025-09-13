package name.lyrablock.util.render.transition

import name.lyrablock.util.math.TimeUtils

@Suppress("unused")
// A value that transitions over time.
class TransitionValue<T: TransitionValue.Transitionable<T>>(
    defaultValue: T,
    val timeFunction: TimeFunction = TimeFunction.LINEAR
) {
    // A transitionable value must be able to be added and subtracted with the same type,
    // and also able to be divided by a Double to scale it.
    interface Transitionable<T> {
        operator fun plus(that: T): T
        operator fun minus(that: T): T
        operator fun div(that: Double): T
    }

    var startValue: T = defaultValue
    var targetValue: T = defaultValue
    var startTime: Long = 0L
    var transitionTime: Int = 0

    /**
     * Starts a transition to the target value over the specified time.
     *
     * If it is started during an existing transition, it interrupts the existing one.
     *
     * @param target The target value to transition to.
     * @param time The time in milliseconds over which to transition.
     */
    fun startTransition(target: T, time: Int) {
        startValue = currentValue()
        targetValue = target
        startTime = TimeUtils.getEpochMilliseconds()
        transitionTime = time
    }

    fun currentValue(): T {
        val elapsed = TimeUtils.getEpochMilliseconds() - startTime

        if (elapsed > startTime) return targetValue

        val progress = timeFunction.invoke(elapsed.toDouble())
        return startValue + (targetValue - startValue) / progress
    }
}
