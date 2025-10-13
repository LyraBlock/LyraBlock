package app.lyrablock.orion.components

class SingletonCollectorScope<T> {
    private val children = mutableListOf<T>()

    fun collectChildren() = children

    operator fun T.unaryPlus() {
        children.add(this@unaryPlus)
    }
}
