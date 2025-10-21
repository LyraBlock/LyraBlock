package app.lyrablock.lyra

import org.reflections.Reflections

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LyraModule() {
    companion object {
        fun load(packageName: String) {
            val reflections = Reflections(packageName)
            val annotated = reflections.getTypesAnnotatedWith(LyraModule::class.java)

            annotated.forEach {
                if (it.isAnnotationPresent(ByMixin::class.java)) {
                    // Works as `continue`.
                    return@forEach
                }
                try {
                    val kClass = it.kotlin
                    // Here calls `init`.
                    val obj = kClass.objectInstance
                    if (obj == null) {
                        LyraBlockClient.LOGGER.warn("Found illegal module ${it.name}.")
                        return@forEach
                    }
                    LyraBlockClient.LOGGER.debug("Loaded ${it.name}.")
                } catch (error: Throwable) {
                    LyraBlockClient.LOGGER.error("Failed to load ${it.name}!", error)
                }
            }
        }
    }

    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class ByMixin
}
