package name.lyrablock

import org.reflections.Reflections

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class LyraModule() {
    companion object {
        fun load(packageName: String) {
            val reflections = Reflections(packageName)
            val annotated = reflections.getTypesAnnotatedWith(LyraModule::class.java)

            annotated.forEach {
                try {
                    val kClass = it.kotlin
                    // Here calls `init`.
                    val obj = kClass.objectInstance
                    if (obj == null) {
                        LyraBlockClient.LOGGER.warn("Skipped non-object module: ${it.name}.")
                        return@forEach
                    }
                    LyraBlockClient.LOGGER.info("Loading LyraBlock module: ${it.name}.")
                } catch (error: Throwable) {
                    LyraBlockClient.LOGGER.error("Failed to load: ${it.name}.", error)
                }
            }
        }
    }
}
