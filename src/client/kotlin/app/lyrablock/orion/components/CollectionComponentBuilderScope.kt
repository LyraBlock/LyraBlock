package app.lyrablock.orion.components

import app.lyrablock.orion.OrionComponent

class CollectionComponentBuilderScope {
    private val children = mutableListOf<OrionComponent>()

    fun getChildren() = children

    operator fun OrionComponent.unaryPlus() {
        children.add(this@unaryPlus)
    }
}
