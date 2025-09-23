package app.lyrablock.event

@Retention(AnnotationRetention.SOURCE)
@Suppress("unused", "PropertyName")
annotation class InvokedBy(val `mixin class`: kotlin.reflect.KClass<*>)
