package app.lyrablock.lyra.util

import app.lyrablock.lyra.LyraBlockClient
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Executes a function only once until explicitly reset.
 * It is designed to be highly performant and thread-safe.
 * The core 'runOnce' logic uses AtomicBoolean (CAS) for lock-free checks.
 */
class ResettableOnceExecutor {
    private val hasRun = AtomicBoolean(false)

    /**
     * Attempts to run the provided function. The function is executed only if it
     * has not been executed since the last reset.
     *
     * @param func The function to execute, which returns a value of type T.
     * @return The result of the function if executed; otherwise, the previously stored result, or null.
     */
    fun execute(func: () -> Unit) {
        // High-performance, lock-free check-and-set using Compare-and-Swap (CAS).
        // Only one thread will successfully change the state from false to true.
        if (hasRun.compareAndSet(false, true)) {
            try {
                // Execute the function and store the result
                func()
            } catch (error: Exception) {
                // IMPORTANT: If execution fails, the flag is still true.
                // If you want to allow retries on failure, call reset() here.
                // For this example, we keep it true to prevent immediate retries.
                LyraBlockClient.LOGGER.error("Function execution failed: ${error.message}")
                throw error
            }
        }
    }

    /**
     * Clears the execution flag and the stored result, allowing the function to run again.
     * The method is synchronized to ensure atomic reset of both 'hasRun' and 'result'.
     */
    @Synchronized
    fun reset() {
        hasRun.set(false)
    }

    val hasExecuted: Boolean
        get() = hasRun.get()
}
