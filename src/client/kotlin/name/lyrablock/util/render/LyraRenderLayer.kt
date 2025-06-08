package name.lyrablock.util.render

import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderPhase

object LyraRenderLayer {
    val LINES_SEETHROUGH: RenderLayer = RenderLayer.of(
        "lyra_lines_seethrough",
        RenderLayer.DEFAULT_BUFFER_SIZE,
        false,
        true,
        RenderPipelines.DEBUG_FILLED_BOX,
        RenderLayer.MultiPhaseParameters.builder()
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false)
    )
}
