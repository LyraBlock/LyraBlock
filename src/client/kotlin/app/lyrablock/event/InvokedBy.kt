package app.lyrablock.event

import kotlin.reflect.KClass

@Retention(AnnotationRetention.SOURCE)
@Suppress("unused", "PropertyName")
annotation class InvokedBy(val `mixin class`: KClass<*>)
