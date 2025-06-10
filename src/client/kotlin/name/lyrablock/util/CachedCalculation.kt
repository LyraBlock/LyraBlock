package name.lyrablock.util

/**
 * A utility object for caching the results of calculations based on a dependency.
 * This is useful for expensive calculations that depend on some state that doesn't change often.
 */
@Suppress("unused")
object CachedCalculation {
    val cache = mutableMapOf<Int, Any>()
    val cachedDependency = mutableMapOf<Int, Int>()

    fun <T> calculateCached(dependency: Int, function: () -> T): T {
        // We identify the function by its hash code.
        // Will this cause bug... Wait...
        val functionHash = function.hashCode()

        @Suppress("UNCHECKED_CAST")
        if (cachedDependency.getOrDefault(functionHash, null) == dependency)
            return cache[functionHash] as T

        val result = function.invoke()
        cache[functionHash] = result as Any
        cachedDependency[functionHash] = dependency
        return result
    }
}
