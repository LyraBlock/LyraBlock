package app.lyrablock.lyra.util.render

import net.minecraft.client.gl.RenderPipelines
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderPhase
import java.util.*

object LyraRenderLayer {
    val LINES_SEE_THROUGH: RenderLayer = RenderLayer.of(
        "lyra_lines_see_through",
        RenderLayer.DEFAULT_BUFFER_SIZE,
        false,
        true,
        LyraRenderPipelines.LINES_SEE_THROUGH,
        RenderLayer.MultiPhaseParameters.builder()
            .lineWidth(RenderPhase.LineWidth(OptionalDouble.of(1.0)))
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false)
    )

    val QUADS_SEE_THROUGH: RenderLayer = RenderLayer.of(
        "lyra_quads_see_through",
        RenderLayer.DEFAULT_BUFFER_SIZE,
        false,
        true,
        LyraRenderPipelines.QUADS_SEE_THROUGH,
        RenderLayer.MultiPhaseParameters.builder()
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false)
    )

    val QUADS: RenderLayer = RenderLayer.of(
        "lyra_quads",
        RenderLayer.DEFAULT_BUFFER_SIZE,
        false,
        true,
        RenderPipelines.DEBUG_QUADS,
        RenderLayer.MultiPhaseParameters.builder()
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false)
    )

    val FILLED: RenderLayer = RenderLayer.of(
        "lyra_filled",
        RenderLayer.DEFAULT_BUFFER_SIZE,
        false,
        true,
        RenderPipelines.DEBUG_FILLED_BOX,
        RenderLayer.MultiPhaseParameters.builder()
            .layering(RenderPhase.VIEW_OFFSET_Z_LAYERING)
            .build(false)
    )

    fun getLinesSeeThrough() = LINES_SEE_THROUGH
    fun getQuadsSeeThrough() = QUADS_SEE_THROUGH
}
