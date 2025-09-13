package name.lyrablock.util.render.widget

interface Widget {
    val width: Int
    val height: Int

    fun isHovered(x: Int, y: Int) = mousePosition.let { (mouseX, mouseY) ->
        (mouseX in x..x + width) && (mouseY in y..y + height)
    }

    companion object {
        // I am so smart.
        var mousePosition: Pair<Int, Int> = -1 to -1
    }
}
