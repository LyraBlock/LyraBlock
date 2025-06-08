package name.lyrablock.util.render

import com.mojang.blaze3d.pipeline.RenderPipeline
import com.mojang.blaze3d.platform.DepthTestFunction
import net.minecraft.client.gl.RenderPipelines
import net.minecraft.util.Identifier

object LyraRenderPipelines {
    val LINES_SEE_THROUGH = RenderPipelines.register(RenderPipeline.builder(RenderPipelines.RENDERTYPE_LINES_SNIPPET)
        .withLocation(Identifier.of("lyra", "pipelines/lines_see_through"))
        .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
        .build())
}
