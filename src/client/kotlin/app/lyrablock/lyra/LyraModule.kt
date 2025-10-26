package app.lyrablock.lyra

import org.reflections.Reflections

/**
 * Object with this annotated will be loaded upon client initializes.
 * This is used for event registration.
 *
 * The methods in modules should be `private` whenever they can.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LyraModule(val priority: Int = 0) {
    companion object {
        fun load(packageName: String) {
            val reflections = Reflections(packageName)
            val annotated = reflections.getTypesAnnotatedWith(LyraModule::class.java)

            for (target in annotated.sortedBy { it.getAnnotation(LyraModule::class.java).priority }) {
                if (target.isAnnotationPresent(ByMixin::class.java))
                    continue

                try {
                    val kClass = target.kotlin
                    // Here calls `init`.
                    val obj = kClass.objectInstance
                    if (obj == null) {
                        LyraBlockClient.LOGGER.warn("Found illegal module ${target.name}.")
                        continue
                    }
                    LyraBlockClient.LOGGER.debug("Loaded ${target.name}.")
                } catch (error: Throwable) {
                    LyraBlockClient.LOGGER.error("Failed to load ${target.name}!", error)
                }
            }
        }
    }

    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ByMixin
}
