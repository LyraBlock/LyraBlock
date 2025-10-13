package app.lyrablock.orion.components

class PairedCollectionScope<A, B> {
    private val children = mutableMapOf<A, B>()

    fun collectChildren() = children

    operator fun A.plus(value: B) {
        children[this@plus] = value
    }
}
