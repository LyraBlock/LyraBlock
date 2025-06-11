package name.lyrablock.util.render.waypoint

import name.lyrablock.util.math.LyraColor
import name.lyrablock.util.render.LyraRenderLayer
import name.lyrablock.util.render.WorldRenderDSL.renderBlockOutline
import name.lyrablock.util.render.WorldRenderDSL.renderLineFromCrosshair
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents

object WaypointController {
    init {
        WorldRenderEvents.LAST.register(::render)
    }

    fun render(context: WorldRenderContext) {
//        context.renderLineFromCrosshair(LyraRenderLayer.getLinesSeeThrough(), 0, -60, 0, LyraColor.WHITE)
//        context.renderBlockOutline(LyraRenderLayer.getLinesSeeThrough(), 0, -60, 0, LyraColor.WHITE)
    }
}
