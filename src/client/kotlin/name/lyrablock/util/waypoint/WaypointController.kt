package name.lyrablock.util.waypoint

import name.lyrablock.util.render.DrawContextDSL.drawLine
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.RenderTickCounter
import net.minecraft.util.Identifier


object WaypointController {
    val IDENTIFIER = Identifier.of("lyra", "waypoint")

    init {
        HudLayerRegistrationCallback.EVENT.register {
            it.attachLayerAfter(IdentifiedLayer.MISC_OVERLAYS, IDENTIFIER, ::render)
        }
    }

    fun render(context: DrawContext, tickDeltaManager: RenderTickCounter) {
//        context.drawLine(10f, 10f, 20f, 20f)
    }
}
