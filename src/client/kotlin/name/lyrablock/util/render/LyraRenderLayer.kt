package name.lyrablock.util.render

import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.RenderPhase
import net.minecraft.client.render.VertexFormat
import net.minecraft.client.render.VertexFormats

object LyraRenderLayer {
    val LINES_SEETHROUGH = RenderLayer.of(
        "lyra_lines_seethrough",
        VertexFormats.POSITION_COLOR,
        VertexFormat.DrawMode.LINES,
        RenderLayer.DEFAULT_BUFFER_SIZE,
        RenderLayer.MultiPhaseParameters.builder()
            .lineWidth(RenderPhase.LineWidth.FULL_LINE_WIDTH)
            .transparency(RenderLayer.TRANSLUCENT_TRANSPARENCY)
            .depthTest(RenderLayer.ALWAYS_DEPTH_TEST)
            .build(false)
    )
}
