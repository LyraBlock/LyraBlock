package name.lyrablock.util

import kotlin.reflect.KFunction

/**
 * A utility object for caching the results of calculations based on a dependency.
 * This is useful for expensive calculations that depend on some state that doesn't change often.
 */
object CachedCalculation {
    val cache = mutableMapOf<Int, Any>()
    val cachedDependency = mutableMapOf<Int, Int>()

    fun <T> calculateCached(dependency: Any, function: KFunction<T>): T {
        // We identify the function by its hash code.
        // Will this cause bug... Wait...
        val functionHash = function.hashCode()
        // We tell if the dependency has changed by comparing its hash code.
        val dependencyHash = dependency.hashCode()

        @Suppress("UNCHECKED_CAST")
        if (cachedDependency.getOrDefault(functionHash, null) == dependencyHash)
            return cache[functionHash] as T

        val result = function.call()
        cache[functionHash] = result as Any
        cachedDependency[functionHash] = dependencyHash
        return result
    }
}
