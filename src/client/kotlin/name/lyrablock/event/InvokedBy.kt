package name.lyrablock.event

@Retention(AnnotationRetention.SOURCE)
@Suppress("unused")
annotation class InvokedBy(val clazz: kotlin.reflect.KClass<*>)
