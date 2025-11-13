package app.lyrablock.lyra.util.render

import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.client.renderer.RenderStateShard
import net.minecraft.client.renderer.RenderType
import java.util.*

object LyraRenderLayer {
    val LINES_SEE_THROUGH: RenderType = RenderType.create(
        "lyra_lines_see_through",
        RenderType.TRANSIENT_BUFFER_SIZE,
        false,
        true,
        LyraRenderPipelines.LINES_SEE_THROUGH,
        RenderType.CompositeState.builder()
            .setLineState(RenderStateShard.LineStateShard(OptionalDouble.of(1.0)))
            .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(false)
    )

    val QUADS_SEE_THROUGH: RenderType = RenderType.create(
        "lyra_quads_see_through",
        RenderType.TRANSIENT_BUFFER_SIZE,
        false,
        true,
        LyraRenderPipelines.QUADS_SEE_THROUGH,
        RenderType.CompositeState.builder()
            .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(false)
    )

    val QUADS: RenderType = RenderType.create(
        "lyra_quads",
        RenderType.TRANSIENT_BUFFER_SIZE,
        false,
        true,
        RenderPipelines.DEBUG_QUADS,
        RenderType.CompositeState.builder()
            .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(false)
    )

    val FILLED: RenderType = RenderType.create(
        "lyra_filled",
        RenderType.TRANSIENT_BUFFER_SIZE,
        false,
        true,
        RenderPipelines.DEBUG_FILLED_BOX,
        RenderType.CompositeState.builder()
            .setLayeringState(RenderStateShard.VIEW_OFFSET_Z_LAYERING)
            .createCompositeState(false)
    )

    fun getLinesSeeThrough() = LINES_SEE_THROUGH
    fun getQuadsSeeThrough() = QUADS_SEE_THROUGH
}
