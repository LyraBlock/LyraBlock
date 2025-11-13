package app.lyrablock.lyra.util.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import net.minecraft.client.renderer.RenderPipelines
import net.minecraft.resources.ResourceLocation

object LyraRenderPipelines {
    val LINES_SEE_THROUGH: RenderPipeline = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
        .withLocation(ResourceLocation.fromNamespaceAndPath("lyra", "pipelines/lines_see_through"))
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .build())

    val QUADS_SEE_THROUGH: RenderPipeline = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
        .withLocation(ResourceLocation.fromNamespaceAndPath("lyra", "pipelines/quads_see_through"))
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .withCull(false)
        .build())
}
