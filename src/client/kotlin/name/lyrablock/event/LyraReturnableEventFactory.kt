package name.lyrablock.event

// Do we actually need a return value?
open class LyraReturnableEventFactory<T, R> {


    val handlerList = mutableListOf<(T) -> R>()

    fun dispatch(event: T) {
        handlerList.forEach {
            it.invoke(event)
        }
    }

    fun register(callback: (T) -> R) {
        handlerList.add(callback)
    }
}

typealias LyraEventFactory<T> = LyraReturnableEventFactory<T, Unit>