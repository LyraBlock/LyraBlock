package app.lyrablock.lyra.util.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.util.Identifier

object LyraRenderPipelines {
    val LINES_SEE_THROUGH: RenderPipeline = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.RENDERTYPE_LINES_SNIPPET)
        .withLocation(Identifier.of("lyra", "pipelines/lines_see_through"))
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .build())

    val QUADS_SEE_THROUGH: RenderPipeline = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
        .withLocation(Identifier.of("lyra", "pipelines/quads_see_through"))
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .withCull(false)
        .build())
}
